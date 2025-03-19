package command;

import exception.MissingCompulsoryParameter;
import exception.TravelDiaryException;
import exception.WrongMachineState;
import parser.Parser;
import trip.TripManager;

import java.util.Objects;

public class CommandFactory {

    public Command getCommand(TripManager tripManager, Parser parser, int fsmValue) throws TravelDiaryException, NumberFormatException,
            MissingCompulsoryParameter, WrongMachineState {
        parser.fsmValue = fsmValue;
        if (parser.fsmValue == 0) {
            switch (parser.getHashmap().get("command")) {
            case "bye":
                parser.isExit = true;
                break;
            case "view_trip":
                tripManager.viewTrips();
                break;
            case "select":
                int index = Integer.parseInt(parser.getHashmap().get("index"));
                //tripManager.selectTrip(Integer.parseInt(parser.getHashmap().get("index")));
                parser.fsmValue = 1;
                return new SelectTripCommand(index);
            case "add_trip":
                // tripManager.addTrip(parser.getHashmap().get("name"),
                // parser.getHashmap().get("description"), parser.getHashmap().get("location"));
                String name = parser.getHashmap().get("name");
                String description = parser.getHashmap().get("description");
                String location = parser.getHashmap().get("location");
                return new AddTripCommand(name, description, location);
            case "delete":
                tripManager.deleteTrip(Integer.parseInt(parser.getHashmap().get("index")));
                break;
            case "menu":
                parser.fsmValue = 0;
                System.out.println("back to main menu");
                break;
            default:
                throw new WrongMachineState(fsmValue);

            }
        } else if (parser.fsmValue == 1) {
            String command = parser.getHashmap().get("command");
            if (!Objects.equals(command, "bye") && !Objects.equals(command, "menu")) {
                System.out.println("current trip: " + tripManager.getSelectedTrip() + "\n");
            }
            switch (command) {
            case "bye":
                parser.isExit = true;
                break;
            case "add_photo":
                tripManager.getSelectedTrip().album.addPhoto(parser.getHashmap().get("filepath"),
                        parser.getHashmap().get("photoname"), parser.getHashmap().get("caption"),
                        parser.getHashmap().get("location"));
                break;
            case "select":
                tripManager.getSelectedTrip().album.selectPhoto(Integer.parseInt(parser.getHashmap().get("index")));
                break;
            case "view_photo":
                tripManager.getSelectedTrip().album.viewPhotos();
                break;
            case "delete":
                tripManager.getSelectedTrip().album.deletePhoto(Integer.parseInt(parser.getHashmap().get("index")));
                break;
            case "menu":
                parser.fsmValue = 0;
                tripManager.getSelectedTrip().album.setSelectedPhoto(null);
                tripManager.setSelectedTrip(null);
                System.out.println("back to main menu");
                break;
            default:
                throw new WrongMachineState(fsmValue);
            }
        }
        throw new TravelDiaryException();
    }
}
