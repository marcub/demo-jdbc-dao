package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int sellersInserted = preparedStatement.executeUpdate();

            if(sellersInserted > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int sellerInsertedId = resultSet.getInt(1);
                    seller.setId(sellerInsertedId);
                }
                DB.closeResultSet(resultSet);
            }
            else {
                throw new DbException("Unexpected error! No sellers inserted.");
            }
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("" +
                    "UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                return instantiateSeller(resultSet, department);
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(department);
        return seller;
    }

    @Override
    public List<Seller> findAll() {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("" +
                    "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");

            List<Seller> allSellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(resultSet.next()) {
                Department auxDep = map.get(resultSet.getInt("DepartmentId"));
                if(auxDep == null) {
                    auxDep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), auxDep);
                }
                allSellers.add(instantiateSeller(resultSet, auxDep));
            }
            return allSellers;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(statement);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("" +
                    "SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");
            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            List<Seller> sellersFilteredByDepartment = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(resultSet.next()) {
                Department auxDep = map.get(resultSet.getInt("DepartmentId"));
                if(auxDep == null) {
                   auxDep = instantiateDepartment(resultSet);
                   map.put(resultSet.getInt("DepartmentId"), auxDep);
                }
                sellersFilteredByDepartment.add(instantiateSeller(resultSet, auxDep));
            }
            return sellersFilteredByDepartment;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }
    }
}
