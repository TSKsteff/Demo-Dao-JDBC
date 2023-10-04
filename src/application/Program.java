package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=======\tTeste 1: seller findById\t==========");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("========\tTeste 2: seller findByDepartment\t=======");
		Department department = new Department(2,null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("========\tTeste 3: seller findAll\t=======");
		list = sellerDao.findAll();
		
		for (Seller obj : list) {
			System.out.println(obj);
		
		}
		
		System.out.println("========\tTeste 4: seller insert\t=======");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Insered! Id: "+newSeller.getId());
		
		
		System.out.println("========\tTeste 5: seller update\t=======");
		 seller = sellerDao.findById(3);
		sellerDao.insert(newSeller);
		seller.setName("Mathias Weine");
		sellerDao.update(seller);
		System.out.println("update completed");
	
		System.out.println("========\tTeste 5: seller update\t=======");
		System.out.println("Enter id for delete test:  ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete Completed");
		
		sc.close();
	}
}
