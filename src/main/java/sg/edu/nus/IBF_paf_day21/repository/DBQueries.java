package sg.edu.nus.IBF_paf_day21.repository;

public class DBQueries {
    
    public static final String SELECT_ALL_CUSTOMERS = "select id, company, first_name, last_name from customer limit ? , ?;";
    public static final String SELECT_CUSTOMER_BY_ID = "select id, company, first_name, last_name from customer where id = ?;";
    public static final String SELECT_ORDERS_FOR_CUSTOMERS = 
        "select c.id as customer_id, c.company, o.id as order_id,o.ship_name, o.shipping_fee from customer c, orders o where c.id = o.customer_id and customer_id = ?;";
}
