import model.Event;
import model.User;
import service.LogService;
import service.LogServiceImpl;
import service.UserService;
import service.UserServiceImpl;

import java.util.List;
import java.util.Map;

public class ParserApp {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        LogService logService = new LogServiceImpl (userService);

        Map<User, Map<String, List<Event>>> log = logService.prepareStructure();

//        logService.printAllUsersLogs();

        userService.search(log);

      }
}
