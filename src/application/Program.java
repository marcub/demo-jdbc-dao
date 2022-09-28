package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("------TEST SELLER FINDBYID------");
        System.out.println(sellerDao.findById(2));

        System.out.println();

        System.out.println("------TEST SELLER INSERT------");
        Seller sellerInsert = new Seller(1, "Joana", "joana@gmail.com", new Date(), 2500.00, new Department(2, null));
        sellerDao.insert(sellerInsert);
        System.out.println("Id seller inserted: " + sellerInsert.getId());

        System.out.println();

        System.out.println("------TEST SELLER UPDATE------");
        Seller sellerUpdate = new Seller(5, "Emilly", "emilly@gmail.com", new Date(), 5500.00, new Department(4, null));
        sellerDao.update(sellerUpdate);

        System.out.println();

        System.out.println("------TEST SELLER DELETE------");
        sellerDao.deleteById(15);

        System.out.println();

        System.out.println("------TEST SELLER FINDALL------");
        List<Seller> allSellers = sellerDao.findAll();
        allSellers.forEach(System.out::println);

        System.out.println();

        System.out.println("------TEST SELLER FINDBYDEPARTMENT------");
        List<Seller> filteredByDepartmentSellers = sellerDao.findByDepartment(new Department(2, null));
        filteredByDepartmentSellers.forEach(System.out::println);

        System.out.println();

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("------TEST DEPARTMENT FINDBYID------");
        System.out.println(departmentDao.findById(2));

        System.out.println();

        System.out.println("------TEST DEPARTMENT INSERT------");
        Department departmentInsert1 = new Department(null, "Desks");
        Department departmentInsert2 = new Department(null, "Foods");
        departmentDao.insert(departmentInsert1);
        departmentDao.insert(departmentInsert2);
        System.out.println("Id seller inserted 1: " + departmentInsert1.getId());
        System.out.println("Id seller inserted 2: " + departmentInsert2.getId());

        System.out.println();

        System.out.println("------TEST DEPARTMENT UPDATE------");
        Department departmentUpdate = new Department(1, "Notebooks");
        departmentDao.update(departmentUpdate);

        System.out.println();

        System.out.println("------TEST DEPARTMENT DELETE------");
        departmentDao.deleteById(6);

        System.out.println();

        System.out.println("------TEST DEPARTMENT FINDALL------");
        List<Department> allDepartments = departmentDao.findAll();
        allDepartments.forEach(System.out::println);

    }
}
