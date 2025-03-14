/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

 import java.sql.DriverManager;
 import java.sql.Connection;
 import java.sql.Statement;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.io.File;
 import java.io.FileReader;
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.util.List;
 import java.util.ArrayList;
 import java.lang.Math;

 /**
  * This class defines a simple embedded SQL utility class that is designed to
  * work with PostgreSQL JDBC drivers.
  *
  */
 public class PizzaStore {

    // reference to physical database connection.
    private Connection _connection = null;

    // handling the keyboard inputs through a BufferedReader
    // This variable can be global for convenience.
    static BufferedReader in = new BufferedReader(
                                 new InputStreamReader(System.in));

    // Login for current session
    private String _login = null;

    /**
     * Creates a new instance of PizzaStore
     *
     * @param hostname the MySQL or PostgreSQL server hostname
     * @param database the name of the database
     * @param username the user name used to login to the database
     * @param password the user login password
     * @throws java.sql.SQLException when failed to make a connection.
     */
    public PizzaStore(String dbname, String dbport, String user, String passwd) throws SQLException {

       System.out.print("Connecting to database...");
       try{
          // constructs the connection URL
          String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
          System.out.println ("Connection URL: " + url + "\n");

          // obtain a physical connection
          this._connection = DriverManager.getConnection(url, user, passwd);
          System.out.println("Done");
       }catch (Exception e){
          System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
          System.out.println("Make sure you started postgres on this machine");
          System.exit(-1);
       }//end catch
    }//end PizzaStore

    /**
     * Method to execute an update SQL statement.  Update SQL instructions
     * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
     *
     * @param sql the input SQL string
     * @throws java.sql.SQLException when update failed
     */
    public void executeUpdate (String sql) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the update instruction
       stmt.executeUpdate (sql);

       // close the instruction
       stmt.close ();
    }//end executeUpdate

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and outputs the results to
     * standard out.
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQueryAndPrintResult (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       /*
        ** obtains the metadata object for the returned result set.  The metadata
        ** contains row and column info.
        */
       ResultSetMetaData rsmd = rs.getMetaData ();
       int numCol = rsmd.getColumnCount ();
       int rowCount = 0;

       // iterates through the result set and output them to standard out.
       boolean outputHeader = true;
       while (rs.next()){
        if(outputHeader){
          for(int i = 1; i <= numCol; i++){
             System.out.print(rsmd.getColumnName(i) + "\t");
          }
          System.out.println();
          outputHeader = false;
        }
          for (int i=1; i<=numCol; ++i)
             System.out.print (rs.getString (i) + "\t");
          System.out.println();
          ++rowCount;
       }//end while
       stmt.close();
       return rowCount;
    }//end executeQuery

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and returns the results as
     * a list of records. Each record in turn is a list of attribute values
     *
     * @param query the input query string
     * @return the query result as a list of records
     * @throws java.sql.SQLException when failed to execute the query
     */
    public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       /*
        ** obtains the metadata object for the returned result set.  The metadata
        ** contains row and column info.
        */
       ResultSetMetaData rsmd = rs.getMetaData ();
       int numCol = rsmd.getColumnCount ();
       int rowCount = 0;

       // iterates through the result set and saves the data returned by the query.
       boolean outputHeader = false;
       List<List<String>> result  = new ArrayList<List<String>>();
       while (rs.next()){
         List<String> record = new ArrayList<String>();
       for (int i=1; i<=numCol; ++i)
          record.add(rs.getString (i));
         result.add(record);
       }//end while
       stmt.close ();
       return result;
    }//end executeQueryAndReturnResult

    /**
     * Method to execute an input query SQL instruction (i.e. SELECT).  This
     * method issues the query to the DBMS and returns the number of results
     *
     * @param query the input query string
     * @return the number of rows returned
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int executeQuery (String query) throws SQLException {
        // creates a statement object
        Statement stmt = this._connection.createStatement ();

        // issues the query instruction
        ResultSet rs = stmt.executeQuery (query);

        int rowCount = 0;

        // iterates through the result set and count nuber of results.
        while (rs.next()){
           rowCount++;
        }//end while
        stmt.close ();
        return rowCount;
    }

    /**
     * Method to fetch the last value from sequence. This
     * method issues the query to the DBMS and returns the current
     * value of sequence used for autogenerated keys
     *
     * @param sequence name of the DB sequence
     * @return current value of a sequence
     * @throws java.sql.SQLException when failed to execute the query
     */
    public int getCurrSeqVal(String sequence) throws SQLException {
    Statement stmt = this._connection.createStatement ();

    ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
    if (rs.next())
       return rs.getInt(1);
    return -1;
    }

    /**
     * Method to close the physical connection if it is open.
     */
    public void cleanup(){
       try{
          if (this._connection != null){
             this._connection.close ();
          }//end if
       }catch (SQLException e){
          // ignored.
       }//end try
    }//end cleanup

    /**
     * The main execution method
     *
     * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
     */
    public static void main (String[] args) {
       if (args.length != 3) {
          System.err.println (
             "Usage: " +
             "java [-classpath <classpath>] " +
             PizzaStore.class.getName () +
             " <dbname> <port> <user>");
          return;
       }//end if

       Greeting();
       PizzaStore esql = null;
       try{
          // use postgres JDBC driver.
          Class.forName ("org.postgresql.Driver").newInstance ();
          // instantiate the PizzaStore object and creates a physical
          // connection.
          String dbname = args[0];
          String dbport = args[1];
          String user = args[2];
          esql = new PizzaStore (dbname, dbport, user, "");

          boolean keepon = true;
          while(keepon) {
             // These are sample SQL statements
             System.out.println("MAIN MENU");
             System.out.println("---------");
             System.out.println("1. Create user");
             System.out.println("2. Log in");
             System.out.println("9. < EXIT");
             String authorisedUser = null;
             switch (readChoice()){
                case 1: CreateUser(esql); break;
                case 2: authorisedUser = LogIn(esql); break;
                case 9: keepon = false; break;
                default : System.out.println("Unrecognized choice!"); break;
             }//end switch
             if (authorisedUser != null) {
               boolean usermenu = true;
               while(usermenu) {
                 System.out.println("MAIN MENU");
                 System.out.println("---------");
                 System.out.println("1. View Profile");
                 System.out.println("2. Update Profile");
                 System.out.println("3. View Menu");
                 System.out.println("4. Place Order"); //make sure user specifies which store
                 System.out.println("5. View Full Order ID History");
                 System.out.println("6. View Past 5 Order IDs");
                 System.out.println("7. View Order Information"); //user should specify orderID and then be able to see detailed information about the order
                 System.out.println("8. View Stores");

                 //**the following functionalities should only be able to be used by drivers & managers**
                 System.out.println("9. Update Order Status");

                 //**the following functionalities should ony be able to be used by managers**
                 System.out.println("10. Update Menu");
                 System.out.println("11. Update User");

                 System.out.println(".........................");
                 System.out.println("20. Log out\n");
                 switch (readChoice()){
                    case 1: viewProfile(esql); break;
                    case 2: updateProfile(esql); break;
                    case 3: viewMenu(esql); break;
                    case 4: placeOrder(esql); break;
                    case 5: viewAllOrders(esql); break;
                    case 6: viewRecentOrders(esql); break;
                    case 7: viewOrderInfo(esql); break;
                    case 8: viewStores(esql); break;
                    case 9: updateOrderStatus(esql); break;
                    case 10: updateMenu(esql); break;
                    case 11: updateUser(esql); break;



                    case 20: usermenu = false; break;
                    default : System.out.println("Unrecognized choice!"); break;
                 }
               }
             }
          }//end while
       }catch(Exception e) {
          System.err.println (e.getMessage ());
       }finally{
          // make sure to cleanup the created table and close the connection.
          try{
             if(esql != null) {
                System.out.print("Disconnecting from database...");
                esql.cleanup ();
                System.out.println("Done\n\nBye !");
             }//end if
          }catch (Exception e) {
             // ignored.
          }//end try
       }//end try
    }//end main

    public static void Greeting(){
       System.out.println(
          "\n\n*******************************************************\n" +
          "              User Interface      	               \n" +
          "*******************************************************\n");
    }//end Greeting

    /*
     * Reads the users choice given from the keyboard
     * @int
     **/
    public static int readChoice() {
       int input;
       // returns only if a correct value is given.
       do {
          System.out.print("Please make your choice: ");
          try { // read the integer, parse it and break.
             input = Integer.parseInt(in.readLine());
             break;
          }catch (Exception e) {
             System.out.println("Your input is invalid!");
             continue;
          }//end try
       }while (true);
       return input;
    }//end readChoice

     /*
     * Reads the users choice given from the keyboard
     * @int
     **/
    public static double readFloatChoice() {
      double input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Double.parseDouble(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readFloatChoice


    public static int readChoiceWithin(int min, int max) {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());

            if (!(min <= input && input <= max)) {
               System.out.println("Your input is invalid! Please enter a number between " + min + " and " + max + ".");
               continue;
            }

            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);

      return input;
   }//end readChoice

    /*
     * Creates a new user
     **/
    public static void CreateUser(PizzaStore esql){
       try {
          System.out.print("\nEnter login: ");
          String login = in.readLine();
          while (login == null || login.trim().isEmpty()) {
             System.out.println("\nLogin cannot be empty. Please try again.");
             login = in.readLine();
          }

         String query = "SELECT U.login FROM Users U WHERE U.login = '" + login + "';";
         int rowCount = esql.executeQuery(query);

         if (rowCount > 0) {
            System.out.println("Login is already taken. Please try again.");
            return;
         }

          System.out.print("\nEnter password: ");
          String password = in.readLine();
          while (password == null || password.trim().isEmpty()) {
             System.out.println("\nPassword cannot be empty. Please try again.");
             password = in.readLine();
          }

          System.out.print("\nEnter phoneNumber: ");
          String phoneNum = in.readLine();
          while (phoneNum == null || phoneNum.trim().isEmpty() || !phoneNum.matches("\\d{10}")) {
             System.out.println("\nPhone number cannot be empty and must be 10 consecutive digits (Ex: 1234567890). Please try again.");
             phoneNum = in.readLine();
          }

          query = "INSERT INTO Users (login, password, role, favoriteItems, phoneNum) VALUES ('" + login + "', '" + password + "', 'customer', NULL, '" + phoneNum + "');";

          esql.executeUpdate(query);
          System.out.println("\nUser successfully created!\n");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }//end CreateUser

    /*
     * Check log in credentials for an existing user
     * @return User login or null is the user does not exist
     **/
    public static String LogIn(PizzaStore esql){
       try {
          System.out.print("\nEnter login: ");
          String login = in.readLine();
          while (login == null || login.trim().isEmpty()) {
             System.out.println("\nLogin cannot be empty. Please try again.");
             login = in.readLine();
          }

          System.out.print("\nEnter password: ");
          String password = in.readLine();
          while (password == null || password.trim().isEmpty()) {
             System.out.println("\nPassword cannot be empty. Please try again.");
             password = in.readLine();
          }

          String query = "SELECT U.login FROM Users U WHERE U.login = '" + login + "' AND U.password = '" + password + "';";
          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          if (result.size() > 0) {
             esql.setLogin(login);
             System.out.println("\nLogin successful!\n");
             return login;
          }
          else {
             System.out.println("\nA user with the specific login and password does not exist. Please try again.\n");
          }

          return null;
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
          return null;
       }
    }//end

 // Rest of the functions definition go in here

    public static void viewProfile(PizzaStore esql) {
       try {
          String currentUser = esql.getLogin();
          String query = "SELECT U.favoriteItems, U.phoneNum FROM Users U WHERE U.login = '" + currentUser + "';";

          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          System.out.println("\nProfile details:");
          for (List<String> row : result) {
             System.out.println("\tFavorite Items: " + row.get(0));
             System.out.println("\tPhone Number: " + row.get(1));
             System.out.print("\n");
          }
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updateProfile(PizzaStore esql) {
       try {
          System.out.println("\nOPTIONS: ");
          System.out.println("1. Update password");
          System.out.println("2. Update phone number");
          System.out.println("3. Update favorite item");
          System.out.println("4. Exit");

          switch (readChoice()) {
             case 1:
                updatePassword(esql);
                break;
             case 2:
                updatePhoneNumber(esql);
                break;
             case 3:
                updateFavoriteItems(esql);
                break;
             case 4:
                break;
          }
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void viewMenu(PizzaStore esql) {
       try {
          String itemType = askUserAndTrim("Enter the type of items you want to see (e.g., \"entree\").\nIf you want to see all items (regardless of type), enter \"all\": ");

          System.out.println();

          System.out.print("Enter the maximum price of items you want to see.\nIf you want to see all items (regardless of price), enter \"0\": \n");

          double priceLimit = readFloatChoice();

          System.out.print("\n\nWhat order do you want to see the items in?\n1. Ascending price order\n2. Descending price order\n\nPlease type a number: ");

          int priceOrder = readChoiceWithin(1, 2);

          String query = "SELECT * FROM Items I WHERE ";

            if (!itemType.equals("all")) {
               // We use a LIKE to deal with potential leading spaces.
               query += "I.typeOfItem LIKE '%" + itemType + "'";
            } else {
               query += "1=1";
            }

            query += " AND ";

            if (priceLimit > 0) {
               query += "I.price <= " + priceLimit;
            } else {
               query += "1=1";
            }

            query += " ORDER BY I.price ";

            if (priceOrder == 1) {
               query += "ASC";
            } else {
               query += "DESC";
            }

            query += ";";



          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String itemName = row.get(0);
            String ingredients = row.get(1);
            String typeOfItem = row.get(2);
            String price = row.get(3);
            String description = row.get(4);

            print("Item: " + itemName + "\n");
            print("    Type of item: " + typeOfItem + "\n");
            print("    Price: " + price + "\n");
            print("    Description: " + description + "\n");
            print("    Ingredients: " + ingredients + "\n\n");
          }

          if (result.size() == 0) {
            String message = "No items found";

            if (!itemType.equals("all")) {
               message += " of type \"" + itemType + "\"";
            }

            if (priceLimit > 0) {
               message += " under $" + priceLimit;
            }

            message += ".\n";

            print(message);
          }
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void print(String s) {
      System.out.print(s);
    }

    public static void placeOrder(PizzaStore esql) {
      try {
         print("Enter the ID of the store you want to order from.\n");
         int storeID = readChoice();

         String query = "SELECT S.storeID FROM Store S WHERE S.storeID = " + storeID + ";";
         List<List<String>> storeResult = esql.executeQueryAndReturnResult(query);

         if (storeResult.size() == 0) {
            print("Store not found.\n");
            return;
         }

         List<String> itemNames = new ArrayList<String>();
         List<Integer> quantities = new ArrayList<Integer>();
         double totalPrice = 0.0;

         while (true) {
            String itemName = askUserAndTrim("Enter the name of the item you want to order.\nType \"done\" if you are finished adding items: ");

            if (itemName.equals("done")) {
               break;
            }

            query = "SELECT I.price FROM Items I WHERE I.itemName = '" + itemName + "';";
            List<List<String>> itemResult = esql.executeQueryAndReturnResult(query);
            if (itemResult.size() == 0) {
               print("Item not found.\n");
               continue;
            }

            double price = parseDoubleOr(itemResult.get(0).get(0), 0.0);


            print("\nEnter desired quantity: ");

            int quantity = readChoice();

            itemNames.add(itemName);

            quantities.add(quantity);

            totalPrice += quantity * price;

            print("\n");
         }

         String login = esql.getLogin();

         query = "INSERT INTO FoodOrder (login, storeID, totalPrice, orderTimestamp, orderStatus) VALUES ('" + login + "', " + storeID + ", " + totalPrice + ", NOW(), 'incomplete') RETURNING orderID;";

         List<List<String>> orderIDResult = esql.executeQueryAndReturnResult(query);

         if (orderIDResult.isEmpty() || orderIDResult.get(0).isEmpty()) {
            print("Error: Could not retrieve order ID.\n");
            return;
         }

         int orderID = Integer.parseInt(orderIDResult.get(0).get(0)); // Extract orderID

         for (int i = 0; i < itemNames.size(); ++i) {
            String itemName = itemNames.get(i);
            int quantity = quantities.get(i);

            query = "INSERT INTO ItemsInOrder (orderID, itemName, quantity) VALUES (" + orderID + ", '" + itemName + "', " + quantity + ");";

            esql.executeUpdate(query);
         }

         print("\n\nOrder successfully placed!\n\n");
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static int parseIntOr(String s, int fallback) {
      try {
         return Integer.parseInt(s);
      }
      catch (Exception dontCare) {
         return fallback;
      }
    }

    public static double parseDoubleOr(String s, double fallback) {
      try {
         return Double.parseDouble(s);
      }
      catch (Exception dontCare) {
         return fallback;
      }
    }

    public static void viewAllOrders(PizzaStore esql) {
      try {
         String login = esql.getLogin();

         // Get role
         String query = "SELECT U.role FROM Users U WHERE U.login = '" + login + "';";
         List<List<String>> roleResult = esql.executeQueryAndReturnResult(query);
         String role = roleResult.get(0).get(0).trim();

         if (role.trim().equals("manager") || role.trim().equals("driver")) {
            viewAllOrdersAssumingUserIsManagerOrDriver(esql);
         } else {
            viewAllOrdersAssumingUserIsCustomer(esql);
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewAllOrdersAssumingUserIsManagerOrDriver(PizzaStore esql) {
      try {
         String customerLogin = askUserAndTrim("Enter the login name of the customer whose orders you want to see. Type \"all\" to see the orders of all customers.\nLogin name: ");

         String query = "SELECT * FROM FoodOrder F";

         if (!customerLogin.equals("all")) {
            query += " WHERE F.login = '" + customerLogin + "'";
         }

         query += ";";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         print("\n\nORDER IDS:\n\n");

         for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String orderID = row.get(0);
            String login = row.get(1);
            String storeID = row.get(2);
            String totalPrice = row.get(3);
            String orderTimestamp = row.get(4);
            String orderStatus = row.get(5);

            print(orderID + "\n");
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewAllOrdersAssumingUserIsCustomer(PizzaStore esql) {
      try {
         String login = esql.getLogin();

         String query = "SELECT * FROM FoodOrder F WHERE F.login = '" + login + "';";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         print("\n\nORDER IDS:\n\n");

         for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String orderID = row.get(0);
            // String login = row.get(1);
            String storeID = row.get(2);
            String totalPrice = row.get(3);
            String orderTimestamp = row.get(4);
            String orderStatus = row.get(5);

            print(orderID + "\n");
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewRecentOrders(PizzaStore esql) {
      try {
         String login = esql.getLogin();

         // Get role
         String query = "SELECT U.role FROM Users U WHERE U.login = '" + login + "';";
         List<List<String>> roleResult = esql.executeQueryAndReturnResult(query);
         String role = roleResult.get(0).get(0).trim();

         if (role.equals("manager") || role.equals("driver")) {
            viewRecentOrdersAssumingUserIsManagerOrDriver(esql);
         } else {
            viewRecentOrdersAssumingUserIsCustomer(esql);
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewRecentOrdersAssumingUserIsManagerOrDriver(PizzaStore esql) {
      try {
         String customerLogin = askUserAndTrim("Enter the login name of the customer whose orders you want to see: ");

         String query = "SELECT * FROM FoodOrder F";

         query += " WHERE F.login = '" + customerLogin + "' ORDER BY F.orderTimestamp DESC LIMIT 5;";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         print("\n\nORDER IDS:\n\n");

         for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String orderID = row.get(0);
            String login = row.get(1);
            String storeID = row.get(2);
            String totalPrice = row.get(3);
            String orderTimestamp = row.get(4);
            String orderStatus = row.get(5);

            print(orderID + "\n");
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewRecentOrdersAssumingUserIsCustomer(PizzaStore esql) {
      try {
         String login = esql.getLogin();

         String query = "SELECT * FROM FoodOrder F WHERE F.login = '" + login + "' ORDER BY F.orderTimestamp DESC LIMIT 5;";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         print("\n\nORDER IDS:\n\n");

         for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String orderID = row.get(0);
            // String login = row.get(1);
            String storeID = row.get(2);
            String totalPrice = row.get(3);
            String orderTimestamp = row.get(4);
            String orderStatus = row.get(5);

            print(orderID + "\n");
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewOrderInfo(PizzaStore esql) {
      try {
         String login = esql.getLogin();

         // Get role
         String query = "SELECT U.role FROM Users U WHERE U.login = '" + login + "';";
         List<List<String>> roleResult = esql.executeQueryAndReturnResult(query);
         String role = roleResult.get(0).get(0).trim();
         boolean canSeeOtherUsersOrders = role.equals("manager") || role.equals("driver");

         print("Enter the order ID you want to see.");
         int targetOrderID = readChoice();

         query = "SELECT * FROM FoodOrder F WHERE F.orderID = '" + targetOrderID + "';";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         if (result.size() == 0) {
            print("Order not found.\n");
            return;
         }

         // This should iterate at most once.
         for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String orderID = row.get(0);
            String orderLogin = row.get(1);
            String storeID = row.get(2);
            String totalPrice = row.get(3);
            String orderTimestamp = row.get(4);
            String orderStatus = row.get(5);

            if (!(canSeeOtherUsersOrders || orderLogin.equals(login))) {
               print("This order does not belong to you.\n");
               return;
            }

            print("\nORDER INFO\n");
            print("Order ID: " + orderID + "\n");
            print("Customer login: " + orderLogin + "\n");
            print("Store ID: " + storeID + "\n");
            print("Total price: " + totalPrice + "\n");
            print("Order timestamp: " + orderTimestamp + "\n");
            print("Order status: " + orderStatus + "\n\n");
         }
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void viewStores(PizzaStore esql) {
      try {
         String query = "SELECT * FROM Store S ORDER BY S.storeID ASC;";
         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         print("\n\nSTORES:\n\n");

         for (int i = 0; i < result.size(); ++i) {
            List<String> row = result.get(i);

            String storeID = row.get(0);
            String address = row.get(1);
            String city = row.get(2);
            String state = row.get(3);
            String isOpen = row.get(4);
            String reviewScore = row.get(5);

            print("Store ID: " + storeID + "\n");
            print("    Address: " + address + "\n");
            print("    City: " + city + "\n");
            print("    State: " + state + "\n");
            print("    Is open: " + isOpen + "\n");
            print("    Review score: " + reviewScore + "\n\n");
         }

      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
    }

    public static void updateOrderStatus(PizzaStore esql) {
      try {
         String login = esql.getLogin();

         String query = "SELECT U.role FROM Users U WHERE U.login = '" + login + "';";

         List<List<String>> result = esql.executeQueryAndReturnResult(query);

         String role = result.get(0).get(0);

         if (!role.trim().equals("manager") || !role.trim().equals("driver")) {
            System.out.println("Insufficient privileges. Please ask a manager or driver for assistance.");
            return;
         }

         System.out.print("Enter order ID to update: ");
         Integer orderID = Integer.parseInt(in.readLine());

         System.out.println("Is the order complete?");
         System.out.println("Options:");
         System.out.println("1. Yes");
         System.out.println("2. No");

         switch (readChoice()) {
            case 1:
               query = "UPDATE FoodOrder SET orderStatus = 'completed' WHERE orderID = " + orderID + ";";
               break;
            case 2:
               query = "UPDATE FoodOrder SET orderStatus = 'incomplete' WHERE orderID = " + orderID + ";";
               break;
            default:
               System.out.println("Invalid choice. Order status not updated.");
               return;
         }

         esql.executeUpdate(query);

         System.out.println("Order '" + orderID + "' was updated!");
      }
      catch (Exception e) {
         System.err.println(e.getMessage());
      }
   }

    public static void updateMenu(PizzaStore esql) {
       try {
          String login = esql.getLogin();

          String query = "SELECT U.role FROM Users U WHERE U.login = '" + login + "';";
          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          String role = result.get(0).get(0);

          if (!role.trim().equals("manager")) {
             System.out.println("\nInsufficient privileges.");
             return;
          }

          System.out.println("\nOPTIONS: ");
          System.out.println("1. Update item");
          System.out.println("2. Add item");
          System.out.println("3. Exit");

          switch (readChoice()) {
             case 1:
                updateMenuItem(esql);
                break;
             case 2:
                addMenuItem(esql);
                break;
             case 3:
                break;
          }
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updateUser(PizzaStore esql) {
       try {
          String login = esql.getLogin();

          String query = "SELECT U.role FROM Users U WHERE U.login = '" + login + "';";
          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          String role = result.get(0).get(0);

          if (!role.trim().equals("manager")) {
             System.out.println("\nInsufficient privileges. Please contact a manager to update login/role.");
             return;
          }

          System.out.println("\nOPTIONS: ");
          System.out.println("1. Update user's login");
          System.out.println("2. Update user's role");
          System.out.println("3. Exit");

          switch (readChoice()) {
             case 1:
                updateUserLogin(esql);
                break;
             case 2:
                updateUserRole(esql);
                break;
             case 3:
                break;
          }
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    /**
     * Sets the login for current session after successful log-in.
     */
    public void setLogin(String login) {
       _login = login;
    }

    /**
     * Returns the login for current session.
     */
    public String getLogin() {
       return _login;
    }

    public static String askUserAndTrim(String question) {
         System.out.print(question);
         String answer;

         while (true) {
            try {
               answer = in.readLine();
               break;
            } catch (java.io.IOException dontCare) {}
         }

         while (answer == null || answer.trim().isEmpty()) {
            System.out.println("\nYour answer cannot be empty. Please try again.");
            try {
               answer = in.readLine();
            } catch (java.io.IOException dontCare) {}

         }
         return answer.trim();
    }

    /**
     * Updates the user's password using current password and login
     */
    public static void updatePassword(PizzaStore esql) {
       try {
          System.out.print("Enter your current password: ");
          String password = in.readLine();
          while (password == null || password.trim().isEmpty()) {
             System.out.println("\nPassword cannot be empty. Please try again.");
             password = in.readLine();
          }

          String login = esql.getLogin();
          String query = "SELECT U.login FROM Users U WHERE U.login = '" + login + "' AND U.password = '" + password + "';";
          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          if (result.size() < 1) {
             System.out.println("\nIncorrect password. Please try again.\n");
             return;
          }

          System.out.print("Enter your new password: ");
          password = in.readLine();
          while (password == null || password.trim().isEmpty()) {
             System.out.println("\nPassword cannot be empty. Please try again.");
             password = in.readLine();
          }

          query = "UPDATE Users SET password = '" + password + "' WHERE login = '" + login + "';";

          esql.executeUpdate(query);

          System.out.println("Password successfully updated!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updatePhoneNumber(PizzaStore esql) {
       try {
          System.out.print("Enter your password: ");
          String password = in.readLine();
          while (password == null || password.trim().isEmpty()) {
             System.out.println("\nPassword cannot be empty. Please try again.");
             password = in.readLine();
          }

          String login = esql.getLogin();
          String query = "SELECT U.login FROM Users U WHERE U.login = '" + login + "' AND U.password = '" + password + "';";
          List<List<String>> result = esql.executeQueryAndReturnResult(query);

          if (result.size() < 1) {
             System.out.println("\nIncorrect password. Please try again.\n");
             return;
          }

          System.out.print("Enter your new phone number: ");
          String phoneNum = in.readLine();
          while (phoneNum == null || phoneNum.trim().isEmpty() || !phoneNum.matches("\\d{10}")) {
             System.out.println("\nPhone number cannot be empty and must be 10 consecutive digits (Ex: 1234567890). Please try again.");
             phoneNum = in.readLine();
          }

          query = "UPDATE Users SET phoneNum = '" + phoneNum + "' WHERE login = '" + login + "';";

          esql.executeUpdate(query);

          System.out.println("Phone number successfully updated!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updateFavoriteItems(PizzaStore esql) {
       try {
          System.out.print("Enter your new favorite item: ");
          String item = in.readLine();
          while (item == null || item.trim().isEmpty()) {
             System.out.println("\nLogin cannot be empty. Please try again.");
             item = in.readLine();
          }

          String query = "SELECT I.itemName FROM Items I WHERE I.itemName = '" + item + "';";
          int rowCount = esql.executeQuery(query);

          if (rowCount < 1) {
             System.out.println("\nItem not found. Please enter a valid item from the menu.\n");
             return;
          }

          query = "UPDATE Users SET favoriteItems = '" + item + "' WHERE login = '" + esql.getLogin() + "';";
          esql.executeUpdate(query);

          System.out.println("Favorite item updated!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updateUserLogin(PizzaStore esql) {
       try {
          System.out.print("Enter user's login: ");
          String login = in.readLine();
          while (login == null || login.trim().isEmpty()) {
             System.out.println("\nLogin cannot be empty. Please try again.");
             login = in.readLine();
          }

          if (login.trim().equals(esql.getLogin())) {
             System.out.println("\nYou are unable to update your own login. Please ask another manager.");
             return;
          }

          String query = "SELECT U.login FROM Users U WHERE U.login = '" + login + "';";
          int rowCount = esql.executeQuery(query);

          if (rowCount < 1) {
             System.out.println("\nUser was not found. Please enter a valid login.\n");
             return;
          }

          System.out.print("Enter user's new login");
          String newLogin = in.readLine();
          while (newLogin == null || newLogin.trim().isEmpty()) {
             System.out.println("\nNew login cannot be empty. Please try again.");
             newLogin = in.readLine();
          }

          query = "UPDATE Users SET login = '" + newLogin + "' WHERE login = '" + login + "';";
          esql.executeUpdate(query);

          System.out.println("User login updated!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updateUserRole(PizzaStore esql) {
       try {
          System.out.print("Enter user's login: ");
          String login = in.readLine();
          while (login == null || login.trim().isEmpty()) {
             System.out.println("\nLogin cannot be empty. Please try again.");
             login = in.readLine();
          }

          if (login.trim().equals(esql.getLogin())) {
             System.out.println("\nYou are unable to update your own role. Please ask another manager.");
             return;
          }

          String query = "SELECT U.login FROM Users U WHERE U.login = '" + login + "';";
          int rowCount = esql.executeQuery(query);

          if (rowCount < 1) {
             System.out.println("\nLogin was not found. Please enter a valid login.\n");
             return;
          }

          System.out.print("Enter user's new role: ");
          String role = in.readLine();
          while (login == null || login.trim().isEmpty()) {
             System.out.println("\nRole cannot be empty. Please try again.");
             login = in.readLine();
          }

          query = "UPDATE Users SET role = '" + role + "' WHERE login = '" + login + "';";
          esql.executeUpdate(query);

          System.out.println("User role updated!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void updateMenuItem(PizzaStore esql) {
       try {
          System.out.print("Enter item to update: ");
          String item = in.readLine();
          while (item == null || item.trim().isEmpty()) {
             System.out.println("\nItem cannot be empty. Please try again.");
             item = in.readLine();
          }

          String query = "SELECT I.itemName FROM Items I WHERE I.itemName = '" + item + "';";
          int rowCount = esql.executeQuery(query);

          if (rowCount < 1) {
             System.out.println("Item not found. Please try again.");
             return;
          }

          System.out.print("Enter field to update: ");
          String field = in.readLine();
          while (field == null || field.trim().isEmpty()) {
             System.out.println("\nField cannot be empty. Please try again.");
             field = in.readLine();
          }

          System.out.println("Enter updated information: ");
          String info = in.readLine();
          while (info == null || info.trim().isEmpty()) {
             System.out.println("\nInformation cannot be empty. Please try again.");
             info = in.readLine();
          }

          query = "UPDATE Item SET " + field + " = " + info + " WHERE itemName = '" + item + "';";

          esql.executeUpdate(query);

          System.out.println("Menu item updated!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

    public static void addMenuItem(PizzaStore esql) {
       try {
          System.out.print("Enter item to add: ");
          String item = in.readLine();
          while (item == null || item.trim().isEmpty()) {
             System.out.println("\nItem cannot be empty. Please try again.");
             item = in.readLine();
          }

          String query = "SELECT I.itemName FROM Items I WHERE I.itemName = '" + item + "';";
          int rowCount = esql.executeQuery(query);

          if (rowCount >= 1) {
             System.out.println("Item already exists. Please try again.");
             return;
          }

          System.out.print("Enter item ingredients: ");
          String ingredients = in.readLine();
          while (ingredients == null || ingredients.trim().isEmpty()) {
             System.out.println("\nIngredients cannot be empty. Please try again.");
             ingredients = in.readLine();
          }

          System.out.print("Enter type of item: ");
          String type = in.readLine();
          while (type == null || type.trim().isEmpty()) {
             System.out.println("\nType of item cannot be empty. Please try again.");
             type = in.readLine();
          }

          System.out.println("Enter item price: ");
          Double price = Double.parseDouble(in.readLine());
          while (price < 0) {
             System.out.println("\nPrice must be a positive value. Please try again.");
             price = Double.parseDouble(in.readLine());
          }

          System.out.print("Enter description: ");
          String description = in.readLine();

          query = "INSERT INTO Items (itemName, ingredients, typeOfItem, price, description) VALUES ('" + item + "', '" + ingredients + "', '" + type + "', " + price + ", '" + description + "');";

          esql.executeUpdate(query);

          System.out.println("Menu item added!");
       }
       catch (Exception e) {
          System.err.println(e.getMessage());
       }
    }

 }//end PizzaStore

