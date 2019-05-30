import com.halfnet.tickeytackeytoe.access.CommandSupplier;
import com.halfnet.tickeytackeytoe.ai.*;
import com.halfnet.tickeytackeytoe.game.*;

/**
 * This class is used as a template for creating your own AI.
 * This is meant to be used by BlueJ IDE
 * @author half/net
 */
public class PlaceholderAI implements CommandSupplier{

    @Override
    public TilePosition chooseNextSubBoard(Game gameBoard) {
        return null; //TODO Change code to perform AI Operations
    }

    @Override
    public TilePosition chooseNextPlay(Game gameBoard) {
        return null; //TODO Change code to perform AI Operations
    }

    public static void main(String[] args){
        AIGame a = new AIGame(new EasyAI(), new PlaceholderAI());
    }
}
