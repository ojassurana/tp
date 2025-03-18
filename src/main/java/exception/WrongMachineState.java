package exception;

public class WrongMachineState extends Exception {
    public WrongMachineState(int fsmValue) {
        System.out.println("you are at " + ((fsmValue == 0) ? "main menu" : "trip menu"));
        if (fsmValue == 0) {
            System.out.println("please select a trip if you want to use this command");
        } else if (fsmValue == 1) {
            System.out.println("please enter 'menu' to go back to main menu level");
        }
    }

}
