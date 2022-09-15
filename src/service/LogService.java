package service;

import model.Action;
import model.ActionState;
import model.Event;
import model.User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface LogService {

    Map<User, Map<String, List<Event>>> prepareStructure();

    Event createEvent(String[] data);

    Action findAction(String action);

    ActionState findActionState(String actionState);

    Scanner readFileLog ();

    void addNewUserOrEventToMainLog (String date, Event event, User user);

    void createNewDateOrEventRecord (String date, Event event, Map<String, List<Event>> userLog);

    void createNewUserRecord (User user, String date, Event event);

    void printAllUsersLogs();

}
