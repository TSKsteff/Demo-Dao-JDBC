package application;


import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartementDao;
import model.entities.Department;

public class DepartementMain {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartementDao depart = DaoFactory.createDeparmentDao();
		System.out.println("============ departement select ===========");
		
		for(Department dp : depart.finall()) {
			System.out.println(dp.toString());
		}
		
		System.out.println("============ departement select By id ===========");
		int id = sc.nextInt();
		depart.findById(id);
		System.out.println("exclusão realizada com succeso");
		
		System.out.println("============ departement insert ===========");
		Department dep = new Department(null, "TI");
		depart.insert(dep);
		System.out.println("Insert efetuado do id"+dep.getId());
		
		System.out.println("============ departement Update ===========");
		dep = depart.findById(3);
		dep.setName("Nuevo");
		depart.update(dep);
		System.out.println("Update complçeto");
		
		sc.close();
	}

}
