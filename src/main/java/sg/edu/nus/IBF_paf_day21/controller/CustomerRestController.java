package sg.edu.nus.IBF_paf_day21.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.IBF_paf_day21.model.Customer;
import sg.edu.nus.IBF_paf_day21.model.Order;
import sg.edu.nus.IBF_paf_day21.repository.CustomerRepository;

@RestController
@RequestMapping(path="/api", produces= MediaType.APPLICATION_JSON_VALUE)
public class CustomerRestController {
    
    CustomerRepository customerRepository;

    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(path="/customers")
    public ResponseEntity<String> getAllCustomers(@RequestParam(required = false, defaultValue="5") String limit, @RequestParam(required=false, defaultValue="0") String offset){

        if(Objects.isNull(limit)) limit = "5";
        if(Objects.isNull(offset)) offset = "0";

        List<Customer> customers = customerRepository.getAllCustomer(Integer.parseInt(limit), Integer.parseInt(offset));
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for(Customer c : customers){
            arrayBuilder.add(c.toJson());
        }

        JsonArray result = arrayBuilder.build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());

    }

    @GetMapping(path="/customer/{customerId}")
    public ResponseEntity<String> getCustomerById(@PathVariable Integer customerId){

        JsonObject result=null;
        try{
            Customer customer  = customerRepository.findCustomerById(customerId);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("customer", customer.toJson());
            result = objectBuilder.build();            
        }catch(IndexOutOfBoundsException e){
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"error_msg\":\"record not found\"}");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());

    }
    
    @GetMapping(path="/customer/{customer_id}/orders")
    public ResponseEntity<String> getOrdersForCustomer(@PathVariable Integer customer_id){

        List<Order> orders = new ArrayList<Order>();

        JsonArray result = null;

        orders = customerRepository.getCustomerOrders(customer_id);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for(Order o : orders){
            arrayBuilder.add(o.toJson());
        }
        result = arrayBuilder.build();

        if (result.size()==0) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error_msg\":\"record not found\"}");
        }
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }
}
