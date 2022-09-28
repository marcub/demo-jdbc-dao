package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller seller = new Seller(1, "Carla", "carla@gmail.com", new Date(), 1450.0, new Department(2, null));

        sellerDao.update(seller);
    }
}
