package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Classes.AddedProduct;
import Classes.Attendance;
import Classes.Client;
import Classes.Employee;
import Classes.Order;
import Classes.Product;
import Classes.Salary;
import Classes.Shift;
import Classes.User;
import Classes.Process;


public class DBConnection {
	////http://localhost:8081/phpmyadmin/
	private final String url="jdbc:mysql://localhost:3306/Market?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
	String query="";
	
	private Connection connect() throws SQLException
    {
        Connection con=DriverManager.getConnection(url,"root","");
        return con;
    }

	public void clearAll() throws SQLException
	{
		
		Statement statement=connect().createStatement();
		List<Employee> employees=getAllEmployees();
		for(int i=0;i<employees.size();i++)		//for every employee
		{
			query="DROP TABLE e"+employees.get(i).getCode()+"";
			statement.execute(query);
		}
		query="DELETE FROM user ";
        statement.execute(query);
        query="DELETE FROM addingprocess ";
        statement.execute(query);
        query="DELETE FROM addingprocesscontent ";
        statement.execute(query);
        query="DELETE FROM delivery ";
        statement.execute(query);
        query="DELETE FROM employee ";
        statement.execute(query);
        query="DELETE FROM product ";
        statement.execute(query);
        query="DELETE FROM returnprocess ";
        statement.execute(query);
        query="DELETE FROM returnprocesscontent ";
        statement.execute(query);
        query="DELETE FROM sellprocess ";
        statement.execute(query);
        query="DELETE FROM sellprocesscontent ";
        statement.execute(query);
        query="DELETE FROM shift ";
        statement.execute(query);
        query="DELETE FROM user ";
        statement.execute(query);

	}
	
	public boolean isTheOwner(String pass) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from user ";
		statement.executeQuery(query);
		int k=0;
		while(statement.getResultSet().next()&&k==0)
        {
			String  password=statement.getResultSet().getString("password");
			if(password.equals(pass))
			{
				return true;
			}
			k++;
        }
		return false;
	}
	public Product getProduct(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from product where code = '"+code+"'";
		statement.executeQuery(query);
		Product product=null;
		while(statement.getResultSet().next())
        {
			String  name=statement.getResultSet().getString("name");
			double  purchPrice=Double.parseDouble(statement.getResultSet().getString("purchPrice"));
			double  price=Double.parseDouble(statement.getResultSet().getString("price"));
			int  amount=Integer.parseInt(statement.getResultSet().getString("amount"));
			int  minAmount=Integer.parseInt(statement.getResultSet().getString("minAmount"));
            product=new Product(code,name,purchPrice,price,amount,minAmount);
        }
		return product;
	}
	
	public  List<Product> getAllProducts() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from product ";
		statement.executeQuery(query);
		List<Product> products=new ArrayList<>();
		while(statement.getResultSet().next())
        {
			String code=statement.getResultSet().getString("code");
			String  name=statement.getResultSet().getString("name");
			double  purchPrice=Double.parseDouble(statement.getResultSet().getString("purchPrice"));
			double  price=Double.parseDouble(statement.getResultSet().getString("price"));
			int  amount=Integer.parseInt(statement.getResultSet().getString("amount"));
			int  minAmount=Integer.parseInt(statement.getResultSet().getString("minAmount"));
            products.add(new Product(code,name,purchPrice,price,amount,minAmount));
        }
		return products;
	}
		
	public boolean correctUser(String name,String password) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from user ";
		statement.executeQuery(query);
		while(statement.getResultSet().next())
        {
			String code=statement.getResultSet().getString("code");
			String  pass=statement.getResultSet().getString("password");
			if(name.equals(code)&&password.equals(pass))
			{
				return true;
			}
        }
		return false;
	}
	
	public boolean thereIsOpenedShift(String userCode) throws SQLException	//if there is opened shift for this user
	{
		Statement statement=connect().createStatement();
		query="select * from shift WHERE end = 'null' ";
		statement.executeQuery(query);
		while(statement.getResultSet().next())	//will enter only if there is opened shift
        {
			if(statement.getResultSet().getString("usercode").equals(userCode))		//if the user code of the opened shift the same with this user code
			{
				return true;
			}
        }
		
		return false;
	}
	
	
	public void createNewShift(User user) throws SQLException
	{
		
		Statement statement=connect().createStatement();
		Shift shift=currentShift();   						//get last shift as object
		String shiftCode=String.valueOf(shiftsNo()+1);
		if(shift!=null)
		{
			query="update shift set end ='"+time()+"' where end='null' ";
            statement.execute(query);
		}
		query="insert into shift values('"+shiftCode+"','"+user.getCode()+"','"+user.getName()+"','0.0','0.0','"+time()+"','null')";
		statement.execute(query);
	}
	
	public void saveSellProcess(List<Order> orders,boolean delivery,Client client) throws SQLException
	{
		Statement statement=connect().createStatement();
		String shiftCode= String.valueOf(shiftsNo());
		String product="";				//make a string of products code with '_' between every product code
		String amount="";				//make a string of amounts with '_' between every product amount
		double total=0.0;				//total value of process	
		double revenue=0.0;			//total revenue value of process
		for(int i=0;i<orders.size();i++)
		{
			product+=orders.get(i).getProductCode();		//add code of every product
			amount+=orders.get(i).getAmount();				//add amount of every product
			total+=orders.get(i).getTotalPrice();			//add total price of every product (amount * price)
			// add revenue of every product --> revenue = amount * (price - purchacePrice)
			revenue+=orders.get(i).getAmount()*(orders.get(i).getProductPrice()-orders.get(i).getProduct().getPurchPrice());
			if(i!=orders.size()-1)
			{
				product+="_";		
				amount+="_";		
			}
		}
		
		//save sell process content
		String processCode=String.valueOf(currentShiftProcessNo()+1);
		query="insert into sellprocesscontent values('"+shiftCode+"','"+processCode+"','"+product+"','"+amount+"')";		//save sell process content
		statement.execute(query);
		
		//save sell process
		String processTime=time();
		String isDelivery="no";		
		if(delivery)
		{
				isDelivery="yes";
		}
		query="insert into sellprocess values('"+shiftCode+"','"+processCode+"','"+total+"','"+processTime+"','"+isDelivery+"')";
		statement.execute(query);
		
		//save delivery detail
		if(delivery)	//if delivery save client information with same code
		{
			query="insert into delivery values('"+shiftCode+"','"+processCode+"','"+client.getName()+"','"+client.getAddress()+"','"+client.getMobile()+"')";
			statement.execute(query);
		}
		
		//update current shift income and revenue
		
		String newShiftRevenue=String.valueOf(currentShift().getTotalIncome()+total);
		String newShiftProfit=String.valueOf(currentShift().getTotalIncome()+revenue);
		
		query="update shift set totalincome ='"+newShiftRevenue+"' ,totalrevenue ='"+newShiftProfit+"' where end='null' ";
        statement.execute(query);					//update current shift income and revenue
        
        //update products amount
        Product Product;
        String Code;
        for(int i=0;i<orders.size();i++)
        {
        	Product=getProduct(orders.get(i).getProductCode());		//get product as object
        	Code=Product.getCode();									//get product code
        	String newAmount=String.valueOf(Product.getAmount()-orders.get(i).getAmount());	//get new amount = (old amount - sold amount)
        	
        	query="update product set amount ='"+newAmount+"'  where code ='"+Code+"' ";
            statement.execute(query);
        }
	}
	
	public void saveReturnProcess(List<Order> orders) throws SQLException
	{
		Statement statement=connect().createStatement();
		String shiftCode= String.valueOf(shiftsNo());
		String product="";				//make a string of products code with '_' between every product code
		String amount="";				//make a string of amounts with '_' between every product amount
		double total=0.0;				//total value of process	
		double revenue=0.0;			//total revenue value of process
		for(int i=0;i<orders.size();i++)
		{
			product+=orders.get(i).getProductCode();		//add code of every product
			amount+=orders.get(i).getAmount();				//add amount of every product
			total+=orders.get(i).getTotalPrice();			//add total price of every product (amount * price)
			// add revenue of every product --> revenue = amount * (price - purchacePrice)
			revenue+=orders.get(i).getAmount()*(orders.get(i).getProductPrice()-orders.get(i).getProduct().getPurchPrice());
			if(i!=orders.size()-1)
			{
				product+="_";		
				amount+="_";		
			}
		}
		
		//save return process content
		String processCode=String.valueOf(currentShiftReturnProcessNo()+1);
		query="insert into returnprocesscontent values('"+shiftCode+"','"+processCode+"','"+product+"','"+amount+"')";		//save sell process content
		statement.execute(query);
		
		
		//save return process
		String processTime=time();
		query="insert into returnprocess values('"+shiftCode+"','"+processCode+"','"+total+"','"+processTime+"')";
		statement.execute(query);
		
		//update current shift income and revenue
		
		String newShiftIncome=String.valueOf(currentShift().getTotalIncome()-total);
		String newShiftRevenue=String.valueOf(currentShift().getTotalIncome()-revenue);
		
		query="update shift set totalincome ='"+newShiftIncome+"' ,totalrevenue ='"+newShiftRevenue+"' where end='null' ";
        statement.execute(query);					//update current shift income and revenue
        
        Product Product;
        String Code;
        for(int i=0;i<orders.size();i++)
        {
        	Product=getProduct(orders.get(i).getProductCode());		//get product as object
        	Code=Product.getCode();									//get product code
        	String newAmount=String.valueOf(Product.getAmount()+orders.get(i).getAmount());	//get new amount = (old amount - sold amount)
        	
        	query="update product set amount ='"+newAmount+"'  where code ='"+Code+"' ";
            statement.execute(query);
        }
	}

	public Shift currentShift() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from shift where end='null' ";
		statement.executeQuery(query);
		Shift shift=null;
		while(statement.getResultSet().next())
        {
			String shiftCode=statement.getResultSet().getString("shiftcode");
			String  userCode=statement.getResultSet().getString("usercode");
			String  userName=statement.getResultSet().getString("username");
			double totalRevenue=Double.parseDouble(statement.getResultSet().getString("totalincome"));
			double totalProfit=Double.parseDouble(statement.getResultSet().getString("usercode"));
			String  start=statement.getResultSet().getString("start");
			String  end=statement.getResultSet().getString("end");
            shift=new Shift(shiftCode,userCode,userName,totalRevenue,totalProfit,start,end);
        }
		return shift;
	}
	
	
	public String time()
	{
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());			//get current date
		DateTimeFormatter timeFormate = DateTimeFormatter.ofPattern("HH:mm");		//get current time
        LocalDateTime now = LocalDateTime.now();
        String time=timeFormate.format(now);
		return (date+" at "+time);		//make time format --> 12-3-2022 at 12:0
	}
	
	public void add(List<AddedProduct> products) throws SQLException
	{
		String code=String.valueOf(addProcessNo()+1);
		String shiftCode=String.valueOf(shiftsNo());
		double totalPurchasing=0;
		double totalSelling=0;
		String content="";
		String amount="";
		for(int i=0;i<products.size();i++)
		{
			totalPurchasing+=products.get(i).getTotalPurchasingPrice();
			totalSelling+=products.get(i).getTotalSelllingPrice();
			content+=products.get(i).getCode();
			amount+=products.get(i).getAmount();			
			if(i!=products.size()-1)
			{
				content+="_";		
				amount+="_";		
			}
		}
		Statement statement=connect().createStatement();
		//create new adding process 
		query="insert into addingprocess values('"+code+"','"+shiftCode+"','"+time()+"','"+totalPurchasing+"','"+totalSelling+"')";	
		statement.execute(query);
		
		//save adding process content
		query="insert into addingprocesscontent values('"+code+"','"+shiftCode+"','"+content+"','"+amount+"')";	
		statement.execute(query);
		
		//update every product
		for(int i=0;i<products.size();i++)
		{
			int newAmount=getProduct(products.get(i).getCode()).getAmount()+products.get(i).getAmount();
			query="update product set purchPrice ='"+products.get(i).getPurchasingPrice()+"'"
					+ ",price='"+products.get(i).getSelllingPrice()+"' "
					+ ",amount='"+String.valueOf(newAmount)+"' where code ='"+products.get(i).getCode()+"' ";
            statement.execute(query);
		}
		
		
	}
	
	public List<Product> endingSoon() throws NumberFormatException, SQLException		//get ending soon products
	{
		Statement statement=connect().createStatement();
		query="select * from product ";
		statement.executeQuery(query);
		List<Product> products=new ArrayList<>();
		while(statement.getResultSet().next())
        {
			if(Integer.parseInt(statement.getResultSet().getString("amount"))<=Integer.parseInt(statement.getResultSet().getString("minAmount"))) 
			{
				String  code=statement.getResultSet().getString("code");
				String  name=statement.getResultSet().getString("name");
				double  purchPrice=Double.parseDouble(statement.getResultSet().getString("purchPrice"));
				double  price=Double.parseDouble(statement.getResultSet().getString("price"));
				int  amount=Integer.parseInt(statement.getResultSet().getString("amount"));
				int  minAmount=Integer.parseInt(statement.getResultSet().getString("minAmount"));
				products.add(new Product(code,name,purchPrice,price,amount,minAmount));
			}
        }
		return products;
	}
	
	public boolean productWithSameCode(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from product where code='"+code+"'";
		statement.executeQuery(query);
		while(statement.getResultSet().next())	//will enter if there is product with same code
        {
			return true;
        }
		return false;
	}
	
	public boolean productWithSameName(String name) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from product where name ='"+name+"'";
		statement.executeQuery(query);
		while(statement.getResultSet().next())	//will enter if there is product with same name
        {
			return true;
        }
		return false;
	}
	
	public void newProduct(Product product) throws SQLException			// save a new product
	{
		Statement statement=connect().createStatement();
		//create new product
		query="insert into product values('"+product.getCode()+"','"+product.getName()+"','"+product.getPurchPrice()+"','"+product.getPrice()+"','"+product.getAmount()+"','"+product.getMinAmount()+"')";	
		statement.execute(query);
	}
	
	public void updateProduct(Product product) throws SQLException			// save a new product
	{
		Statement statement=connect().createStatement();
		//update new product
		query="update product set purchPrice ='"+String.valueOf(product.getPurchPrice())+"'"
				+ ",price='"+String.valueOf(product.getPrice())+"' "
				+ ",minAmount='"+String.valueOf(product.getMinAmount())+"' where code ='"+product.getCode()+"' ";
        statement.execute(query);
	}
	
	public Shift getCurrentShift() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from shift where end='null'";
		statement.executeQuery(query);
		Shift shift=null;
		while(statement.getResultSet().next())
        {
			 String shiftCode=statement.getResultSet().getString("shiftcode");
			 String userCode=statement.getResultSet().getString("usercode");
			 String username=statement.getResultSet().getString("username");
			 double totalIncome=Double.parseDouble(statement.getResultSet().getString("totalincome"));
			 double totalProfit=Double.parseDouble(statement.getResultSet().getString("totalrevenue"));
			 String start=statement.getResultSet().getString("start");
			 String end=time();
			 shift=new Shift(shiftCode,userCode,username,totalIncome,totalProfit,start,end);
        }
		return shift;
	}
	
	public void signOut() throws SQLException
	{
		Statement statement=connect().createStatement();
		//update new product
		query="update shift set end ='"+time()+"' where end = 'null' ";
        statement.execute(query);
	}
	
	public int shiftsNo() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from shift ";
		statement.executeQuery(query);
		int k=0;
		while(statement.getResultSet().next())
        {
			k++;
        }
		return k;
	}
	
	public int currentShiftProcessNo() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from sellprocess where shiftcode ='"+shiftsNo()+"'";
		statement.executeQuery(query);
		int k=0;
		while(statement.getResultSet().next())
        {
			k++;
        }
		return k;
	}
	
	public int currentShiftReturnProcessNo() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from returnprocess where shiftcode ='"+shiftsNo()+"'";
		statement.executeQuery(query);
		int k=0;
		while(statement.getResultSet().next())
        {
			k++;
        }
		return k;
	}
	
	public int addProcessNo() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from addingprocess";
		statement.executeQuery(query);
		int k=0;
		while(statement.getResultSet().next())
        {
			k++;
        }
		return k;
	}
	
	
	public  List<Employee> getAllEmployees() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from employee ";
		statement.executeQuery(query);
		List<Employee> employees=new ArrayList<>();
		while(statement.getResultSet().next())
        {
			String code=statement.getResultSet().getString("code");
			String  name=statement.getResultSet().getString("name");
			String  position=statement.getResultSet().getString("position");
			double  value=Double.parseDouble(statement.getResultSet().getString("value"));
			String  mobile=statement.getResultSet().getString("mobile");
            employees.add(new Employee(code,name,position,value,mobile));
        }
		return employees;
	}
	
	public boolean employeeWithSameCode(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from employee where code='"+code+"'";
		statement.executeQuery(query);
		while(statement.getResultSet().next())	//will enter if there is product with same code
        {
			return true;
        }
		return false;
	}
	
	public boolean employeeWithSameName(String name) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from employee where name ='"+name+"'";
		statement.executeQuery(query);
		while(statement.getResultSet().next())	//will enter if there is product with same name
        {
			return true;
        }
		return false;
	}
	
	public void newEmployee(Employee employee) throws SQLException			// save a new product
	{
		Statement statement=connect().createStatement();
		//create new product
		query="insert into employee values('"+employee.getCode()+"','"+employee.getName()+"','"+employee.getPosition()+"','"+employee.getHourValue()+"','"+employee.getMobile()+"')";	
		statement.execute(query);
		//create table with name (e+employee name) to save attendance -->    day | attendance | departure | hours
		query="CREATE TABLE e"+employee.getCode()+"(day varchar(45) not null,attendance varchar(45) not null, departure varchar(45) not null ,hours varchar(45) not null)";
		statement.execute(query);
		System.out.println("table created");
	}
	
	public Employee getEmployee(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from employee where code = '"+code+"'";
		statement.executeQuery(query);
		Employee employee=null;
		while(statement.getResultSet().next())
        {
			String  name=statement.getResultSet().getString("name");
			String  position=statement.getResultSet().getString("position");
			double  value=Double.parseDouble(statement.getResultSet().getString("value"));
			String  mobile=statement.getResultSet().getString("mobile");
            employee=new Employee(code,name,position,value,mobile);
        }
		return employee;
	}
	
	public void updateEmployee(Employee employee) throws SQLException			// save a new product
	{
		Statement statement=connect().createStatement();
		//update employee
		query="update employee set position ='"+employee.getPosition()+"'"
				+ ",value='"+String.valueOf(employee.getHourValue())+"' "
				+ ",mobile='"+employee.getMobile()+"' where code ='"+employee.getCode()+"' ";
        statement.execute(query);
	}
	
	public void deleteEmployee(Employee employee) throws SQLException			// save a new product
	{
		Statement statement=connect().createStatement();
		query="DROP TABLE e"+employee.getCode()+"";
        statement.execute(query);
        query="delete from employee where code ='"+employee.getCode()+"' ";
        statement.execute(query);
        query="delete from user where code ='"+employee.getCode()+"' ";
        statement.execute(query);
	}
	
	public boolean hasOpenedDay(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from e"+code+" where departure = 'null'";
		statement.executeQuery(query);
		while(statement.getResultSet().next())
        {
			return true;
        }
		return false;
	}
	
	public Attendance getLastAttendance(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from e"+code+" where departure = 'null'";
		statement.executeQuery(query);
		Attendance attendance =null;
		while(statement.getResultSet().next())
        {
			String day=statement.getResultSet().getString("day");
			String attendanc=statement.getResultSet().getString("attendance");
			String departure=statement.getResultSet().getString("departure");
			double hours=Double.parseDouble(statement.getResultSet().getString("hours"));
			attendance=new Attendance(day,attendanc,departure,hours);
        }
		return attendance;
	}
	
	
	public void employeeDeparture(String code,Attendance attendance) throws SQLException
	{
		Statement statement=connect().createStatement();
		//update new product
		query="update e"+code+" set departure ='"+attendance.getDeparture()+"'"
				+ ",hours='"+String.valueOf(attendance.getHours())+"' where departure = 'null' ";
        statement.execute(query);
	}
	
	public void employeeAttendance(String code,Attendance attendance) throws SQLException
	{
		Statement statement=connect().createStatement();
		//update new product
		query="insert into e"+code+" values('"+attendance.getDay()+"','"+attendance.getAttendance()+"','"+attendance.getDeparture()+"','"+attendance.getHours()+"')";	
		statement.execute(query);
	}
	

	public  List<User> getAllUsers() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from user ";
		statement.executeQuery(query);
		List<User> users=new ArrayList<>();
		while(statement.getResultSet().next())
        {

			String code=statement.getResultSet().getString("code");
			String  name=statement.getResultSet().getString("name");
			String  password=statement.getResultSet().getString("password");
            users.add(new User(code,name,password));
		}
		return users;
	}
	
	public boolean isThereUser () throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from user ";
		statement.executeQuery(query);
		while(statement.getResultSet().next())
        {
				return true;
		}
		return false;
	}
	
	public void deleteUser(String name) throws SQLException			// save a new product
	{
		Statement statement=connect().createStatement();
        query="delete from user where name ='"+name+"' ";
        statement.execute(query);
	}
	
	public User getUser(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from user where code = '"+code+"'";
		statement.executeQuery(query);
		User user=null;
		while(statement.getResultSet().next())
        {
			String  name=statement.getResultSet().getString("name");
			String  password=statement.getResultSet().getString("password");
            user=new User(code,name,password);
        }
		return user;
	}
	
	public void newUser(User user) throws SQLException
	{
		Statement statement=connect().createStatement();
		//create new user
		query="insert into user values('"+user.getCode()+"','"+user.getName()+"','"+user.getPass()+"')";	
		statement.execute(query);
	}
	
	public List<Salary> getSalaries() throws SQLException
	{
		List<Salary> salaries=new ArrayList<>();
 		List<Employee> employees=getAllEmployees();
		for(int i=0;i<employees.size();i++)		//for every employee
		{
			double hours=0;
			Statement statement=connect().createStatement();
			query="select * from e"+employees.get(i).getCode()+"";		//get all worked days
			statement.executeQuery(query);
			while(statement.getResultSet().next())
	        {
				hours+=Double.parseDouble(statement.getResultSet().getString("hours"));	//add hours of every day
	        }
			if(hours>0)
			{
				salaries.add(new Salary(employees.get(i).getCode(),employees.get(i).getName(),hours,employees.get(i).getHourValue()));
				
			}
		}
		return salaries;
	}
	
	public void paySalary(Salary salary) throws SQLException
	{
		Attendance attendance=lastWorkDay(salary.getCode());
		Statement statement=connect().createStatement();
        query="delete from e"+salary.getCode()+"";
        statement.execute(query);
        if(attendance!=null)
        {
        	employeeAttendance(salary.getCode(), attendance);
        }
        
	}
	
	public Attendance lastWorkDay(String code) throws SQLException
	{

		Statement statement=connect().createStatement();
		query="select * from e"+code+" where departure = 'null'";
		statement.executeQuery(query);
		Attendance Attendance=null;
		while(statement.getResultSet().next())
        {
				String day=statement.getResultSet().getString("day");
				String  attendance=statement.getResultSet().getString("attendance");
				String  departure=statement.getResultSet().getString("departure");
				double hours=Double.parseDouble(statement.getResultSet().getString("hours"));
	            Attendance=new Attendance(day,attendance,departure,hours);
		}
		return Attendance;
	}
	
	public List<Attendance> getAttendance(String code) throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from e"+code+" ";
		statement.executeQuery(query);
		List<Attendance> Attendance=new ArrayList<>();
		while(statement.getResultSet().next())
        {
				String day=statement.getResultSet().getString("day");
				String  attendance=statement.getResultSet().getString("attendance");
				String  departure=statement.getResultSet().getString("departure");
				double hours=Double.parseDouble(statement.getResultSet().getString("hours"));
	            Attendance.add(new Attendance(day,attendance,departure,hours));
		}
		return Attendance;
	}

	public List<Shift> getAllShifts() throws SQLException
	{
		Statement statement=connect().createStatement();
		query="select * from shift ";
		statement.executeQuery(query);
		List<Shift> shifts=new ArrayList<>();
		while(statement.getResultSet().next())
        {
			String shiftCode=statement.getResultSet().getString("shiftcode");
			String  userCode=statement.getResultSet().getString("usercode");
			String userName=statement.getResultSet().getString("username");
			double  revenue=Double.parseDouble(statement.getResultSet().getString("totalincome"));
			double  profit=Double.parseDouble(statement.getResultSet().getString("totalrevenue"));
			String start=statement.getResultSet().getString("start");
			String  end=statement.getResultSet().getString("end");
            shifts.add(new Shift(shiftCode,userCode,userName,revenue,profit,start,end));
        }
		List<Shift> reverseShifts=new ArrayList<>();
		for(int i=shifts.size()-1;i>=0;i--)
		{
			reverseShifts.add(shifts.get(i));
		}
		return shifts;
	}

	public List<Process> getShiftProcess(Shift shift) throws SQLException
	{

		Statement statement=connect().createStatement();
		
		//get sell process
		query="select * from sellprocess where shiftcode ='"+shift.getShiftCode()+"'";			
		statement.executeQuery(query);
		List<Process> process=new ArrayList<>();
		while(statement.getResultSet().next())
        {
			String processCode=statement.getResultSet().getString("processCode");
			double  total=Double.parseDouble(statement.getResultSet().getString("total"));
			String time=statement.getResultSet().getString("time");
			String  delivery=statement.getResultSet().getString("delivery");
			process.add(new Process(processCode,total,time,"Sell",delivery));
        }
		
		query="select * from returnprocess where shiftcode ='"+shift.getShiftCode()+"'";			
		statement.executeQuery(query);
		while(statement.getResultSet().next())
        {
			String processCode=statement.getResultSet().getString("processCode");
			double  total=Double.parseDouble(statement.getResultSet().getString("total"));
			String time=statement.getResultSet().getString("time");
			process.add(new Process(processCode,total,time,"Return","no"));
        }
		return process;
	}
}
