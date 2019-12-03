package uniDB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tyler Lund
 *
 * Controller is the controller responsible for reading and parsing the user input
 */

public class Controller {

    /**
     * stored instance of available commands
     */
    private List<String> commands;
    private database db;
    public String user;
    private Library library;

    public Controller() throws FileNotFoundException{
        db = new database();
        
        library = new Library();
        List<Room> rooms = new ArrayList<Room>();
        rooms.add(new Room(11));
        rooms.add(new Room(12));
        db.addDorm(new Dorm(rooms, "Friley"));
//        db.getDorm("Friley").createRooms(0, 10, 0);
        
        commands = new ArrayList<>();
        commands.add("get student");
        commands.add("remove student");
        commands.add("add student");
        commands.add("create group");
        commands.add("edit group");
        commands.add("edit student");
        commands.add("pay tuition");
        commands.add("make major");
        commands.add("add adviser");
        commands.add("make class");
        commands.add("change major id");
        commands.add("remove adviser");
        commands.add("change class id");
        commands.add("change class instructor");
        commands.add("add student to class");
        commands.add("add faculty");
        commands.add("remove faculty");
        commands.add("get faculty");
        commands.add("msg student");
        commands.add("msg faculty");
        commands.add("getMsg student");
        commands.add("getMsg faculty");
        commands.add("create dorm");
        commands.add("list dorms");
        commands.add("select housing");
        commands.add("add book");
        commands.add("remove book");
        commands.add("list books");
        commands.add("get overdue books");
        commands.add("check in book");
        commands.add("check out book");
        commands.add("make lot");
        commands.add("add lot space");
        commands.add("remove lot space");
        commands.add("add parker");
        commands.add("remove parker");
        commands.add("change lot id");
        commands.add("list major");
        commands.add("list class");
        commands.add("get swipes");
        commands.add("use swipe");
        commands.add("check times");
        commands.add("check meal");
        commands.add("save");
        commands.add("create student org");
        commands.add("add student to org");
        commands.add("msg Org");
        commands.add("show org");
    }


    /**
     * This method is used to verify that given a username and password there exists a
     * instance of a user that has the corresponding username and password
     * @param username
     * @param password
     * @return
     */
    protected boolean login(String username, String password) {

        //user user = null;
        student stu = db.findStudent(username);
        faculty fac = db.findFaculty(username);
        boolean login = false;

        if(stu != null) {
        	user = username;
        	login = stu.login(username, password);
        	if(login) {
        		return login;
        	} else {
        		System.out.println("Login Failed, invalid credentials");
        		return false;
        	}
        } else if(fac != null) {
        	user = username;
        	login = fac.login(username, password);
        	if(login) {
        		return login;
        	} else {
        		System.out.println("Login Failed, invalid credentials");
        		return false;
        	}
        } else {
        	System.out.println("Login Failed, invalid credentials");
        	return false;
        }
//        if(user != null) {
//            return user.login(username, password);
//        } else {
//        	System.out.println("Login Failed, invalid credentials");
//            return false;
//        }
    }

    /**
     * This method is responsible for parsing commands given through the cmdView and invoking the correct methods
     * @param command
     * @return object string of executed command or error
     */

    public Object parseCommand(String command){
        Pattern pattern = Pattern.compile("^" + createRegexFromList(commands, false) + ":");
        Matcher match = pattern.matcher(command);
        if(match.find()){
            MatchResult result = match.toMatchResult();
            switch(match.group(0)) {
                case "get student:":
                    String studentsFound = "";
                    for(student x: findStudents(command))
                    {
                        if(x == null){
                            studentsFound += "failed to find student";
                        }
                        else {
                            studentsFound += x.toString() + "\n";
                        }
                    }
                    if(studentsFound != null) {
                        return studentsFound;
                    }
                    else{
                        return "Error finding one or more students with given id's";
                    }
                case "add book:":
                    return addBook(command);
                case "remove book:":
                    return removeBook(command);
                case "check in book:":
                    return checkIn(command);
                case "check out book:":
                    return checkOut(command);
                case"get overdue books:":
                    return overDueBooks(command);
                case "list books:":
                    return listBooks(command);
                case "get faculty:":
                    return findFaculty(command);
                case "remove student:":
                    return removeStudent(command);
                case"add faculty:":
                    return createFaculty(command);
                case"remove faculty:":
                    return removeFaculty(command);
                case"add student:":
                    return createStudent(command);
                case"edit student:":
                        return editField(command);
                case"edit group:":
                    if(findGroup(command) != null) {
                        //edit group:groupName field fieldValue
                        editField(findGroup(command), command);
                        return "updated values for group:" + findGroup(command).getGroupName();
                    }
                    else{
                        return "Could not find group with that name";
                    }
                case"create group:":
                    if(createGroup(command) != null) {
                        database.addGroup(createGroup(command));
                        return "successfully added group";
                    }
                    else {return "one or more invalid id's, please retry";}

                case "pay tuition:":
                	student stu = db.findStudent(user);
                	stu.payTuition(command.substring(13));
                    return "Tuition Payed";

                case "msg student:":
                	String name = null;
                    String com = command.replaceFirst("(.*?)\\:", "");
                    Pattern reg = Pattern.compile("([^\\s]+)");
                    Matcher matcher = reg.matcher(com);
                    if(matcher.find() && !matcher.group(0).trim().equals("")){
                        name = matcher.group(0);
                        com = com.replace(name, "");
                    }
                	db.msgStudent(name, user + ": " + com);
                	return "Sent!";

                case "msg faculty:":
                	String namer = null;
                    String comm = command.replaceFirst("(.*?)\\:", "");
                    Pattern rege = Pattern.compile("([^\\s]+)");
                    Matcher matchers = rege.matcher(comm);
                    if(matchers.find() && !matchers.group(0).trim().equals("")){
                        namer = match.group(0);
                        comm = comm.replace(namer, "");
                    }
                	db.msgFacutly(namer, user + ": " + comm);
                	return "Sent!";

                case "getMsg student:":
                	return db.getMsgsStudent(user);

                case "getMsg faculty:":
                	return db.getMsgsStudent(user);
                
                case "use swipe:":
                	return db.useSwipe(user);
                
                case "get swipes:":
                	return db.getMealSwipes(user);
                	
                case "check times:":
                    return db.checkDiningCenter(command.substring(13));
                
                case "check meal:":
                    return db.getMeal(command.substring(12));
                case "save:":
				try {
					String s = db.save();
				} catch (IOException e) {
					e.printStackTrace();
					return "Save files not found";
				}
                	return "Saved!";
                	
                case "make major:":
                    String mc = command.replaceFirst("make major:", "").trim();
                    if(database.findMajor(mc) == null){
                        Major newmajor = new Major(mc);
                        db.addMajor(newmajor);
                        return "major "+newmajor.getId()+" has been made.";
                    }
                    return "major already exists!";
                case "add adviser:": // major = args[0]     faculty = args[1]
                    mc = command.replaceFirst("add adviser:", "");
                    String[] argtwo = mc.trim().split(" ");   // [0] Major   [1] addviser username
                    faculty adv = db.findFaculty(argtwo[1]); Major major1 = db.findMajor(argtwo[0]);
                    if(major1 == null){return argtwo[0]+" is an invalid major id. Please use a valid id.";}
                        if(adv == null){return argtwo[1]+" is an invalid username. Please use a valid username.";}
                            if(major1.addAdviser(argtwo[1])){return argtwo[1]+" has been added as adviser to "+argtwo[0]+" major.";}
                            else{return "addition of "+argtwo[1]+" to adviser list has failed.";}
                case "make class:":
                    mc = command.replaceFirst("make class:", "");
                    argtwo = mc.trim().split(" ");  // [0] major  [1] new class
                    Major ma = db.findMajor(argtwo[0]);
                    if (ma == null) {return"major entered is invalid";}
                    if(ma.makeClass(argtwo[1])){return "class "+argtwo[1]+" has been created";}
                    else{return "Failed to make class";}
                case "change major id:":
                    mc = command.replaceFirst("change major id:", "");
                    argtwo = mc.trim().split(" ");  //[0] major old id  [1] major new id
                    ma = db.findMajor(argtwo[0]); if(ma == null){return "major id is invalid";}
                    ma.changeID(argtwo[1]); return "major id has been changed to "+argtwo[1];
                case "remove adviser:":
                    mc = command.replaceFirst("remove adviser:", "");
                    argtwo = mc.trim().split(" "); //  [0] major  [1] username of adviser to be removed
                    ma = db.findMajor(argtwo[0]); if(ma == null){return "major id is invalid";}
                    adv = db.findFaculty(argtwo[1]); if(adv == null){return "username of adviser does not exist";}
                    if(ma.removeAdvisor(argtwo[1])){return "adviser "+argtwo[1]+" has been removed";}
                    else{return "failed to remove adviser";}
                case "change class id:":
                    mc = command.replaceFirst("change class id:", "");
                    String[] argthree = mc.trim().split(" "); // [0] major  [1] old class id [2] new class id
                    ma = db.findMajor(argthree[0]); if(ma == null){return "major id is invalid";}
                    Major.Class cl = ma.findClass(argthree[1]); if(cl == null){return "class id is invalid";}
                    if(cl.changeClassId(argthree[2])){return "class id has been changed to "+argthree[2];}
                case "change class instructor:":
                    mc = command.replaceFirst("change class instructor:", "");
                    argthree = mc.trim().split(" "); // [0] = major id [1] = class id [2] = faculty user name
                    ma = db.findMajor(argthree[0]); if(ma == null){return "major id is invalid";}
                    cl = ma.findClass(argthree[1]); if(cl == null){return "class id is invalid";}
                    adv = db.findFaculty(argthree[2]); if(adv == null){return "instructor username is invalid";}
                    if(cl.changeclassinstructor(adv)){return "class "+argthree[1]+" instructor has been changed to "+argthree[2];}
                case "add student to class:":
                    mc = command.replaceFirst("add student to class:", "");
                    argthree = mc.trim().split(" "); // [0] = major id [1] = class id [2] = student username
                    ma = db.findMajor(argthree[0]); if(ma == null){return "major id is invalid";}
                    cl = ma.findClass(argthree[1]); if(cl == null){return "class id is invalid";}
                    student st = db.findStudent(argthree[2]); if(st == null){return "student username is invalid";}
                    if(cl.addstudenttoclass(st)){ return "student "+argthree[2]+" successfully added to class "+argthree[1];}
                case"make lot:":
                    mc = command.replaceFirst("make lot:", "").trim();
                    if(Lot.makeLot(mc )) { return "lot "+mc+" has been created.";}
                    return "creation of lot "+mc+" has failed.";
                case"change lot id:":
                    mc = command.replaceFirst("change lot id:", "").trim();
                    argtwo = mc.split(" ");  // [0] old lot id    [1] new lot id
                    Lot lot = db.findLot(argtwo[0]); if(lot == null) {return "Lot id is invalid.";}
                    lot.changLotId(argtwo[1]);
                    return "Lot id has been changed to "+argtwo[1];
                case"remove lot space:":
                    mc = command.replaceFirst("remove lot space:", "").trim();
                    argtwo = mc.split(" ");  // [0] lot id   [1] number of spaces to be removed from lot spaces
                    lot = db.findLot(argtwo[0]); if(lot == null) {return "Lot id is invalid.";}
                    lot.removeLotspace(Integer.valueOf(argtwo[1]));
                    return "Lot spaces removed.";
                case"add lot space:":
                    mc = command.replaceFirst("add lot space:", "").trim();
                    argtwo = mc.split(" ");  // [0] lot id   [1] number of spaces to be add to lot spaces
                    lot = db.findLot(argtwo[0]); if(lot == null) {return "Lot id is invalid.";}
                    lot.addLotSpace(Integer.valueOf(argtwo[1]));
                    return "Lot spaces added.";
                case "add parker:" :
                    mc = command.replaceFirst("add parker:", "").trim();
                    argtwo = mc.split(" ");  // [0] lot id   [1] username of user to be added
                    lot = db.findLot(argtwo[0]); if(lot == null) {return "Lot id is invalid.";}
                    if(lot.addparker(argtwo[1])){return "Parker successfully added.";}
                    return "failed to add parker";
                case "remove parker:":
                    mc = command.replaceFirst("remove parker:", "").trim();
                    argtwo = mc.split(" ");  // [0] lot id   [1] username of user to be removed
                    lot = db.findLot(argtwo[0]); if(lot == null) {return "Lot id is invalid.";}
                    if(lot.removeparker(argtwo[1])){ return "Parker successfully removed.";}
                    return "failed to remove parker";
                case "list major:":
                    mc = command.replaceFirst("list major:", "").trim();
                    ma = db.findMajor(mc); if (ma == null){ return "major id is invalid";}
                    String str = ma.majorToString();
                    System.out.print(str);
                    return "end of Major list";
                case "list class:":
                    mc = command.replaceFirst("list class:", "");
                    argtwo = mc.trim().split(" ");  // [0] major  [1] new class //NOTE CHANGED AN MC TO A CMI
                    ma = db.findMajor(argtwo[0]); if(ma == null){return "major id is invalid";}
                    cl = ma.findClass(argtwo[1]); if(cl == null){return "class id is invalid";}
                    str = cl.classToString();
                    System.out.print(str);
                    return "end of Class list";
                case "create student org:":
                    return createStudentOrg(command);
                case "add student to org:":
                    return addStudentToOrg(command);
                case "msg Org:":
                   return msgOrg(command);
                case "show org:":
                       return findStudentOrg(command);
                case "create dorm:":
                	db.addDorm(new Dorm(command.substring(13)));
                	return "Dorm created";
                
                case "list dorms:":
                	db.listDorms();
                	return "done";
                case "select housing:":
                	
                	String dormName = command.substring(16);
                	Dorm dorm = db.getDorm(dormName);
                	if(dorm != null) {
                		dorm.listOpenRooms();
                		Scanner scan = new Scanner(System.in);
                		
                		while(true) {
                			System.out.print("Select room by id: ");
                			String id = scan.next();
                			Room room = dorm.getRoomById(Integer.parseInt(id));
                			
                			if(room != null) {
                				room.changeCapacity(room.getCapacity() - 1);
                				return "Added to room " + id;
                			} else {
                				System.out.println("Room not found");
                			}
                		}
                	} else {
                		return "Dorm not found";
                	}
                	
            }
        }
        return "invalid command";
    }

    /**
     * This method edits a students attributes by calling the findStudent method of the database object
     * and the editAttribute method of the student object
     * @param command
     */
    public String editField(String command){
        String name = null;
        String com = command.replaceFirst("(.*?)edit student\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
        String[] args = com.split(" ");
        if(name != null && !findStudents(name).isEmpty()) {
            if(args[1].equalsIgnoreCase("Major")){
                if(db.findMajor(args[2]) != null) {
                    findStudents(name).get(0).setMajor(args[2]);
                }
                else{
                    return "Major does not exist";
                }
            }
            else if(args[1].equalsIgnoreCase("gpa")){
                findStudents(name).get(0).setGPA(Double.parseDouble(args[2]));
            }
            else if(args[1].equalsIgnoreCase("classification")){
                findStudents(name).get(0).setClassification(args[2]);
            }
            else {
                findStudents(name).get(0).editAttribute(args[1], args[2]);
            }
        }
        return "successfully updated student";
    }


    /**
     * This method edits multiple student attributes by calling the findGroup method of the database object
     * and then the findStudent object for each student object within the Group object and then finally the editAttribute method in the
     * student object for each instance of student
     * @param group
     * @param command
     */
    public void editField(Group group, String command){
        String name;
        String com = command.replaceFirst("(.*?)edit group\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
        String[] args = com.split(" ");
        for(student s: group.getStudents()){
            s.editAttribute(args[1], args[2]);
        }
    }

    /**
     * This method is responsible for creating a list of students and adding a Group object to the database if all instances of students
     * were found within the database
     * @param command
     * @return Group object containing list of students given from command
     */
    public Group createGroup(String command){
        List<student> found = new ArrayList<>();
        Group group;
        String name = "";
        String com = command.replaceFirst("(.*?)create group\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
        else{
           return null;
        }
        for (String studentId : com.split(",")) {
            if (database.findStudent(studentId.trim()) == null) {
                //student not found
                //group = null;
               // return group;
            }
            else{
                found.add(database.findStudent(studentId));
            }
        }
        group = new Group(name, found);
        return group;
    }

    public String addStudentToOrg(String command){
        String name = "";
        String com = command.replaceFirst("(.*?)\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
        StudentOrg temp = findStudentOrg(name);
        if(temp != null){
            student tempStudent = db.findStudent(com);
            if(tempStudent != null){
                if(temp.isMember(tempStudent)){
                    return "student is already a member of org";
                }
                else {
                    temp.addMember(tempStudent);
                    return "added " + tempStudent.getUsername() + " to " + temp.getOrgName();
                }
            }
            else{
                return "no student with the username " + com;
            }
        }
        else{
            return "no org found with the name " + name;
        }
    }

    public String createStudentOrg(String command) {
        String[] args;
        String com = command.replaceFirst("(.*?)\\:", "");
        if (com.contains(",")) {
            args = com.split(",");
            StudentOrg temp = findStudentOrg(args[0]);
            if (temp != null) {
                return "student org with the name " + args[0] + " already exists";
            } else {
                if(database.findFaculty(args[1]) == null){
                    return "no faculty by that username";
                }
                else {
                    temp = new StudentOrg(args[0], database.findFaculty(args[1]), database.findStudent(user));
                    db.addStudentOrg(temp);
                    return "added student org";
                }
            }
        }
        return "invalid parameters. create student org:name,advisor";
    }

    public String msgOrg(String command){
        String name = "";
        String com = command.replaceFirst("(.*?)\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
        StudentOrg temp = findStudentOrg(name);
        if(temp != null){
            if(!temp.get_president().getUsername().trim().equalsIgnoreCase(user.trim()) || !user.equalsIgnoreCase("admin")) {
                for (student x : temp.getOrgMembers()) {
                    db.msgStudent(x.username, user + ":" + com);
                }
                return "sucessfully messaged " + temp.getOrgName();
            }
            else{
                return "you are not allowed to mass message this group";
            }
        }
        return "no student org found by the name " + name;
    }


    public StudentOrg findStudentOrg(String command){
        StudentOrg studentOrg;
        String name = "";
        String com = command.replaceFirst("(.*?)\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
        if(db.getStudentOrg(name) != null){
            return db.getStudentOrg(name);
        }
        return null;
    }

    /**
     * This method is responsible for retrieving/verifying Group objects from the database object
     * @param command
     * @return Group object
     */
    public Group findGroup(String command){
        //regex for splitting command to find name
        Group group;
        String name = "";
        String com = command.replaceFirst("(.*?)\\:", "");
        Pattern pattern = Pattern.compile("([^\\s]+)");
        Matcher matcher = pattern.matcher(com);
        if(matcher.find() && !matcher.group(0).trim().equals("")){
            name = matcher.group(0);
            com = com.replace(name, "");
        }
       // String name = "group1";
        if(database.findGroup(name) != null){
            return database.findGroup(name);
        }
        return null;
    }

    /**
     * This method is responsible for retrieving a list of students objects by repeatedly calling the findStudent method from the database
     * object
     * @param command
     * @return List of student objects or null if instance of student not found
     */
    public List<student> findStudents(String command){
        //remove eveything before command colon
        List<student> found = new ArrayList<>();
        String com = command.replaceFirst("(.*?)\\:", "");
        if(com.contains(",")) {
            for (String studentId : com.split(",")) {
                if (database.findStudent(studentId) == null) {
                    //student not found
                    return null;
                }
                else{
                    found.add(database.findStudent(studentId));
                }
            }
        }
            else {
                    found.add(database.findStudent(com));
                    return found;
                }

        return found;
    }

    public List<faculty> findFaculty(String command){
        //remove eveything before command colon
        List<faculty> found = new ArrayList<>();
        String com = command.replaceFirst("(.*?)\\:", "");
        if(com.contains(",")) {
            for (String facultyId : com.split(",")) {
                if (database.findFaculty(facultyId) == null) {
                    //faculty not found
                    return null;
                }
                else{
                    found.add(database.findFaculty(facultyId));
                }
            }
        }
        else {
            found.add(database.findFaculty(com));
            return found;
        }

        return found;
    }

    /**
     * This method is responsible for taking a command and parsing its arguments to create a student object which is then added
     * to the database via the addStudent method of the database object
     * @param command
     * @return
     */
    public String createStudent(String command){
        String com = command.replaceFirst("(.*?)add student\\:", "");
        String[] args = com.split(",");
        student temp = new student(args[0], args[1], args[2]);
        db.addStudent(temp);
        return "added Student: Username:" + args[0] + " Password:" + args[1] + " Full Name:" + args[2] + " with id:" + temp.getId();
    }

    public String removeStudent(String command){
        String removed = "";
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        for(String x: args){
            if(findStudents(x) != null){
                db.removeStudent(x);
                removed += "removed student:" + x + "\n";
            }
            else{
                removed += "failed to remove student:" + x + "\n";
            }
        }
        return "Successfully removed students";

    }

    public String addBook(String command){
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        if(args.length != 2){
            return "invalid parameters. add book:title,author";
        }
        else {
            Book temp = new Book(args[0], args[1]);
            library.addBook(temp);
        }
        return "added book";
    }

    public String checkIn(String command){
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        if(args.length != 2){
            return "invalid parameters. check in book:title,author";
        }
        else {
            Book temp = library.findBook(args[0], args[1]);
            if(temp != null){
                student tempStudent = database.findStudent(user);
                return library.checkInBook(tempStudent, temp);
            }
            else{
                return "no book by that title and author";
            }
        }
    }

    public String checkOut(String command){
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        if(args.length != 2){
            return "invalid parameters. check out book:title,author";
        }
        else {
            Book temp = library.findBook(args[0], args[1]);
            if(temp != null){
                student tempStudent = database.findStudent(user);
                if(tempStudent != null) {
                    return library.checkOutBook(tempStudent, temp);
                }
                else{
                    return "must be a student in order to check out book";
                }
            }
            else{
                return "no book by that title and author";
            }
        }
    }

    public String overDueBooks(String command){
        String temp = "";
        for(Map.Entry<Book,student> entry: library.checkForLate().entrySet()){
            temp += "Book:" + entry.getKey().toString() + " Student:" + entry.getValue().getFullname();
        }
        return temp;
    }

    public String listBooks(String command){
        return library.getBooks();
    }

    public String removeBook(String command){
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        if(args.length != 2){
            return "invalid parameters. remove book:title,author";
        }
        else {
           Book temp = library.findBook(args[0], args[1]);
           if(temp != null) {
               library.removeBook(temp);
           }
        }
        return "removed book";
    }

    public String createFaculty(String command){
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        faculty temp = new faculty(args[0], args[1], args[2]);
        db.addFaculty(temp);
        return "added Faculty: Username:" + args[0] + "Password:" + args[1] + "Full Name:" + args[2] + temp.getId();
    }

    public String removeFaculty(String command){
        String removed = "";
        String com = command.replaceFirst("(.*?)\\:", "");
        String[] args = com.split(",");
        for(String x: args){
            if(db.findFaculty(x) != null){
                db.removeFaculty(x);
                removed += "removed faculty:" + x + "\n";
            }
            else{
                removed += "failed to remove faculty:" + x + "\n";
            }
        }
        return "Successfully removed faculty";

    }

    /**
     * This method is responsible for creating a token regex expression form a list of keywords to be used in the
     * controller object in the parseCommand method
     * @param keywords
     * @param caseSensitive
     * @return
     */
    public String createRegexFromList(List<String> keywords, boolean caseSensitive){
        String regex = "";
        if(!caseSensitive){
            regex = "(?i)";
        }

        for(int i = 0; i < keywords.size(); i++){
            if(i > 0)
                regex += "|";
            regex += keywords.get(i);
        }
        return "(" + regex + ")";
    }
}
