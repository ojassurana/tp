package exception;

public class MissingCompulsoryParameter extends Exception {
    public MissingCompulsoryParameter(String[] parameters) {
        System.out.println("missing compulsory parameters:");
        for (String i : parameters) {
            if (i == null) {
                continue;
            }
            System.out.println("- " + i);
        }
    }
}
