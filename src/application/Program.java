package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        List<Seller> sellersFiltered = sellerDao.findByDepartment(new Department(2, null));

        sellersFiltered.forEach(System.out::println);
    }
}
