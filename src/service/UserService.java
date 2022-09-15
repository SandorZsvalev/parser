package service;

import model.Event;
import model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

     List<User> getUserList();

     User getUserByName (String name);

     void findRecordUserByName (Map<User, Map<String, List<Event>>> mainLog);

     void findRecordUserByNameAndDate (Map<User, Map<String, List<Event>>> mainLog);

     void findRecordAllUsersByDate (Map<User, Map<String, List<Event>>> mainLog);

     void search(Map<User, Map<String, List<Event>>> mainLog);

     void findRecordAllUsersByDateAndSortResult (Map<User, Map<String, List<Event>>> mainLog);






}
