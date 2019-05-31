package com.halfnet.tickeytackeytoe.server;

import com.halfnet.tickeytackeytoe.access.AIGame;
import com.halfnet.tickeytackeytoe.access.CommandSupplier;
import com.halfnet.tickeytackeytoe.game.Game;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Host {

    public static final int SERVER_PORT = 34;
    private static final int SERVER_TIMEOUT = 1000;
    private static final int MAX_RANK_LADDER_SIZE = 50;
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final Comparator<AIContainer> aiComp = (a, b) -> b.score - a.score;

    private ServerSocket server;
    private volatile boolean isRunning;
    private volatile boolean shouldRecalculate;
    private final Thread listenThread;
    private final Thread recalcThread;
    private final List<AIContainer> rankLadder;

    public Host() {
        this.listenThread = new Thread(this::listenLoop, "TickeyTackeyToe-Server-listen");
        this.recalcThread = new Thread(this::recalculate, "TickeyTackeyToe-Server-recalculate");
        this.rankLadder = new ArrayList<>();
    }

    public synchronized void start() throws IOException {
        if (this.server != null) {
            throw new IllegalStateException("Server already stopped");
        }
        this.server = new ServerSocket(SERVER_PORT);
        server.setSoTimeout(SERVER_TIMEOUT);
        this.isRunning = true;
        this.listenThread.start();
        this.recalcThread.start();
    }

    public synchronized void stop() throws IOException {
        if (!this.isRunning) {
            return;
        }
        this.isRunning = false;
        this.listenThread.interrupt();
        this.recalcThread.interrupt();
        this.server.close();
    }

    private void recalculate() {
        while (this.isRunning) {
            while (!this.shouldRecalculate || this.rankLadder.size() <= 1) {
                try {
                    Thread.sleep(340);
                } catch (InterruptedException ex) {
                    return;
                }
            }
            synchronized (this.rankLadder) {
                for (AIContainer a : this.rankLadder) {
                    a.score = 0;
                }
                ExecutorService execService = Executors.newFixedThreadPool(POOL_SIZE);
                ArrayList<Future<String>> fut = new ArrayList<>((this.rankLadder.size() * (this.rankLadder.size() + 1)) / 2);
                for (int i = 0; i < this.rankLadder.size() - 1; i++) {
                    for (int j = i + 1; j < this.rankLadder.size(); j++) {
                        final int ii = i, jj = j;
                        fut.add(execService.submit(() -> {
                            return ii + "," + jj + "," + AIGame.quickRun(rankLadder.get(ii).getCommandSupplier(), rankLadder.get(jj).getCommandSupplier());
                        }));
                    }
                }
                for (int i = 0; !fut.isEmpty(); i++) {
                    Future<String> f = fut.get(i);
                    if (f.isDone()) {
                        try {
                            String[] ta = f.get().split(",");
                            AIContainer a = this.rankLadder.get(Integer.parseInt(ta[0]));
                            AIContainer b = this.rankLadder.get(Integer.parseInt(ta[1]));
                            int c = Integer.parseInt(ta[2]);
                            if (c == 1) {
                                synchronized (a) {
                                    a.score++;
                                }
                                synchronized (b) {
                                    b.score++;
                                }
                            } else if (c == 2) {
                                synchronized (a) {
                                    a.score += 2;
                                }
                            } else {
                                synchronized (b) {
                                    b.score += 2;
                                }
                            }
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace(System.err);
                        } finally {
                            f.cancel(true);
                            fut.remove(f);
                        }
                    }
                    if (i >= fut.size() - 1) {
                        i = -1;
                    }
                }
                Collections.sort(this.rankLadder, aiComp);
                this.shouldRecalculate = false;
                System.out.println("RECALC");
                for (AIContainer a : this.rankLadder) {
                    System.out.println(a.getName() + " " + a.score);
                }
                System.out.println(this.rankLadder.size());
            }
        }
    }

    private void pushCS(CommandSupplier cs) {
        synchronized (this.rankLadder) {
            boolean contains = false;
            for (AIContainer a : this.rankLadder) {
                if (a.getName().equals(cs.getClass().getSimpleName())) {
                    a.setCommandSupplier(cs);
                    contains = true;
                }
            }
            if (!contains) {
                this.rankLadder.add(new AIContainer((cs)));
            }
            this.shouldRecalculate = true;
        }
    }

    private void listenLoop() {
        Game g = new Game();
        System.out.println("Server started");
        while (this.isRunning) {
            try (
                    Socket sock = server.accept();
                    ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                    ) {
                Object read = in.readObject();
                if (!(read instanceof CommandSupplier)) {
                    out.write(0);
                    out.flush();
                    continue;
                }
                CommandSupplier cs = (CommandSupplier) read;
                System.out.println("loaded: " + cs.getClass().getSimpleName());
                pushCS(cs);
                out.write(1);
                out.flush();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (IOException ex) {
                if (ex instanceof SocketTimeoutException) {
                    //Do Nothing
                } else {
                    ex.printStackTrace(System.err);
                }
            }
        }
    }
}
