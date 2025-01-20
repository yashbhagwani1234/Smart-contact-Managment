package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepo;
import com.smart.dao.UserRepo;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpSession;

import com.smart.entities.Contact;

@Controller
@RequestMapping("/user")
public class Usercontroller {
	
       @Autowired
       private UserRepo userre;
       
       @Autowired
       private ContactRepo contrepo;
       /*@Model
         public void addcommondata(Model m,Principal)
         {
           String username = principal.getName();
    	   System.out.println(username);
    	   User usern = userre.getUserByUserName(username);
    	   System.out.println("User:-"+usern);
    	   m.addAttribute("user",usern);
         }
        */
       @GetMapping("/index")
       public String dashboard(Model m,Principal principal)
       {
    	   String username = principal.getName();
    	   System.out.println(username);
    	   User usern = userre.getUserByUserName(username);
    	   System.out.println("User:-"+usern);
    	   m.addAttribute("user",usern);
    	   m.addAttribute("title","User dashboard");
    	   return "normal/user_dashboard";
       }
       // for add contact handler
       @GetMapping("/add-contact")
       public String addcon(Model m, Principal principal,HttpSession session)
       {
    	   String username = principal.getName();// it will take the username which is logined
    	   User usern = userre.getUserByUserName(username);
    	   m.addAttribute("user",usern);
    	   m.addAttribute("title","Add contact");
    	   m.addAttribute("contact",new Contact());
    	   Object message = session.getAttribute("message");
    	   if (message != null) {
               // Add the message to the model
               m.addAttribute("message", message);

               // Remove the message from the session so it's shown only once
               session.removeAttribute("message");
           }
    	   return "normal/add_contact";
       }
       
       
       // adding process of add contact 
       @PostMapping("/process-contact")
       public String adcon(@ModelAttribute Contact contact,@RequestParam("image") MultipartFile file,Principal principal,Model m,HttpSession session)
       {
    	   try {
    	   String username = principal.getName();
    	   User usern = userre.getUserByUserName(username);
    	   m.addAttribute("user",usern);
    	   m.addAttribute("title","processed-contact");
    	   m.addAttribute("contact",new Contact());
    	   contact.setUser(usern);
    	   usern.getList().add(contact);// u can see user getlist by user 
    	   
    	   System.out.println("Added to database");
    	   
    	   if(!contact.getImage().isEmpty())
    	   {
               String fileName = contact.getImage().getOriginalFilename();
               contact.setImageName(fileName);
               
               File savefile = new ClassPathResource("static/img").getFile();
               java.nio.file.Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
               
               Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        	   userre.save(usern);
        	   session.setAttribute("message",new Message("Your contact is added !! Add more..","success"));
        	   
    	   }
    	   else {
               contact.setImageName("contact.png");
        	   session.setAttribute("message",new Message("Your contact is added !! Add more..","success"));
               userre.save(usern);

    	   }
    	   
    	   }catch(Exception e)
    	   {
    		   e.printStackTrace();
        	   session.setAttribute("message",new Message("Something is wrong !!","danger"));
    		   return "error_page";
    	   }
    	   System.out.println("Contact:-"+contact);
    	   return "normal/add_contact";
       }
       //per page[5]
       // current page = 0
       @GetMapping("/view-contacts/{page}")
       public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal,HttpSession session) {
           // Retrieve the username of the currently logged-in user
           String username = principal.getName();
           System.out.println("Username: " + username);
           
           // Fetch the user object using the username
           User user = userre.getUserByUserName(username);
           System.out.println("User: " + user);
           
           // Add the user object and page title to the model
           m.addAttribute("user", user);
           m.addAttribute("title","User Contacts");
           Object message = session.getAttribute("message");
    	   if (message != null) {
               // Add the message to the model
               m.addAttribute("message", message);

               // Remove the message from the session so it's shown only once
               session.removeAttribute("message");
           }
           
           // Define the pageable object (page starts at 0, so subtract 1 if input starts at 1)
           Pageable pageable = PageRequest.of(page, 5); // 5 contacts per page
           
           // Retrieve paginated contacts for the user
           Page<Contact> contactPage = contrepo.findContactByuserID(user.getId(), pageable);
           m.addAttribute("contacts", contactPage.getContent()); // List of contacts for the current page
           m.addAttribute("currentPage", page);
           m.addAttribute("totalPages", contactPage.getTotalPages()); // Total pages for pagination
           return  "normal/show_contact";
       }
       @GetMapping("/{Cid}/contact")
       public String viewContact(@PathVariable("Cid") int Cid,Model  m,Principal principal,HttpSession session) {
    	   String username = principal.getName();
    	   User user =  userre.getUserByUserName(username);
    	   System.out.println(Cid);
    	   m.addAttribute("user",user);
    	  
    	  Optional<Contact> conts= contrepo.findById(Cid);
    	   Contact cont = conts.get();
    	   m.addAttribute("title",cont.getFname());

    	   if(user.getId()==cont.getUser().getId())
    	   {
        	   m.addAttribute("model",cont);
    	   }
    	   return "normal/add_details";
       }
       @GetMapping("/delete/{cid}")
       public String deletecon(@PathVariable("cid") int cid,Model m,HttpSession session) {
    	   Optional<Contact> contOp = contrepo.findById(cid);
    	  Contact c =  contOp.get();
    	  contrepo.delete(c);
    	  
    	  //c.setUser(null); //if entries not deleted
    	  session.setAttribute("message",new Message("Contact deleted Sucessfully..","success"));
    	   return "redirect:/user/view-contacts/0";
       }
       
       @PostMapping("/update-contact/{cid}")
       public String updatecon(Model m,Principal principal,@PathVariable("cid") int cid)
       {
    	  String usernam= principal.getName();
    	  User user = userre.getUserByUserName(usernam);
    	   m.addAttribute("title","Update contact");
    	   m.addAttribute("user",user);
    	   Contact cont = contrepo.findById(cid).get();
    	   m.addAttribute("contact",cont);
    	   return "normal/update_form";
    	   
       }
       ///update contact handler
       @PostMapping("/process-update")
       public String updatehndlr(@ModelAttribute Contact contact,@RequestParam("image") MultipartFile file,HttpSession session,Principal principal) {
    	   System.out.println("Name:-"+contact.getFname());
    	   System.out.println("id:-"+contact.getCid());
    	   //old contact details
    	   Contact ocd = contrepo.findById(contact.getCid()).get();
    	   try {
    		   if(!file.isEmpty()) {
    			   
    			   // delelte old photo
    			   File dlt = new ClassPathResource("static/img").getFile();
    			   File dlt1 = new File(dlt,ocd.getImageName());
    			   dlt1.delete();
    			   
    			   //update new photo
    			   File savefile = new ClassPathResource("static/img").getFile();
                   java.nio.file.Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                   Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                   contact.setImageName(file.getOriginalFilename());
    		   }
    		   else {
    			   contact.setImageName(ocd.getImageName());
    		   }
    		   User user = userre.getUserByUserName(principal.getName());
    		   contact.setUser(user);
    		  this.contrepo.save(contact);
    		  session.setAttribute("message",new Message("Your contact is updated ...!!","success"));
    	   }
    	   catch(Exception e)
    	   {
    		   e.printStackTrace();
    	   }
    	   return "redirect:/user/"+contact.getCid()+"/contact";
       }
       @GetMapping("/profile")
       public String userPr(Model m,Principal principal)
       {
    	   String username = principal.getName();
    	   User user = userre.getUserByUserName(username);
    	   m.addAttribute("user",user);
    	   m.addAttribute("title",user.getName()+"profile");
    	   return "normal/profile";
       }
}
