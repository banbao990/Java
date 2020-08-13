/**
 * @author banbao
 */

package evenAB;
import static evenAB.AB.*;

public enum State {
    // AEBE:
    //    a:even, b:even
    AEBE {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AOBE;
            case B: ret = AEBO;
            }
            return ret;
        }
    },
    AEBO {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AOBO;
            case B: ret = AEBE;
            }
            return ret;
        }
    },
    AOBE {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AEBE;
            case B: ret = AOBO;
            }
            return ret;
        }
    },
    AOBO {
        @Override
        State next(AB ab) {
            State ret = null;
            switch(ab) {
            case A: ret = AEBO;
            case B: ret = AOBE;
            }
            return ret;
        }         
    };
    abstract State next(AB ab);
}