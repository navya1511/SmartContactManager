package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ContactRepository contactRepo;
	
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		String userName = principal.getName();
		User user = userRepo.getUserByUserName(userName);
		m.addAttribute("user", user);
	}
	
	@GetMapping("/index")
	public String dashboard(Model m, Principal principal) {
		m.addAttribute("title", "User Dashboard");
		return "dashboard";
	}

	@GetMapping("/addContact")
	public String addContactForm(Model m) {
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		return "normal/addContact";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, 
			Principal principal, HttpSession session) {
		try {
			String userName = principal.getName();
			User user = userRepo.getUserByUserName(userName);
			System.out.println(file.getOriginalFilename());
			if(file.isEmpty()) {
				contact.setImage("user.png");
			}
			else {
				contact.setImage(file.getOriginalFilename());
				System.out.println(file.getOriginalFilename());
				File file1 = new ClassPathResource("/static/images").getFile();
				System.out.println(file1);
				Files.copy(file.getInputStream(),
						Paths.get(file1.getAbsolutePath() + File.separator + file.getOriginalFilename()),
						StandardCopyOption.REPLACE_EXISTING);
			}
			contact.setUser(user);
			user.getContacts().add(contact);
			userRepo.save(user);
			System.out.println(contact);
			session.setAttribute("message", new Message("Contact saved successfully", "alert-success"));
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			session.setAttribute("message", new Message("Try again!!", "alert-danger"));
		}
		return "normal/addContact";
	}
	
	
	@GetMapping("/showContacts/{page}")
	public String showContacts(@PathVariable int page,Model  m, Principal principal) {
		m.addAttribute("title", "Show User contacts");
		String userName = principal.getName();
		User user = userRepo.getUserByUserName(userName);
		Pageable p = PageRequest.of(page, 5);
	  Page<Contact> contacts = contactRepo.findContactsByUserId(user.getId(), p);
	   m.addAttribute("contacts", contacts);
	   m.addAttribute("currentPage", page);
	   m.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/showContacts";
	}
	
	
	//showing particular detail of contact
	@GetMapping("/{id}/contact")
	public String showContact(@PathVariable int id, Model m, Principal p) {
		String userName = p.getName();
		User user = userRepo.getUserByUserName(userName);
		
		
		Optional<Contact> contactOptional = contactRepo.findById(id);
		Contact contact = contactOptional.get();
		if(user.getId() == contact.getUser().getId()) {
			m.addAttribute("contact", contact);
		}
		
		return "normal/showContact";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable int cid, Principal p, HttpSession session) {
		String userName = p.getName();
		User user = userRepo.getUserByUserName(userName);
		Optional<Contact> contactOptional = contactRepo.findById(cid);
		Contact contact = contactOptional.get();
		
		if(user.getId() == contact.getUser().getId()) {
			contact.setUser(null);
			contactRepo.deleteById(cid);
		}
		
		session.setAttribute("message", new Message("Contact Deleted Successfully", "success"));
		
		return "redirect:/user/showContacts/0";
	}
	
	@PostMapping("/updateContact/{cid}")
	public String updateForm(@PathVariable int cid, Model m) {
		Contact contact = contactRepo.findById(cid).get();
		m.addAttribute("contact", contact);
		return "normal/updateForm";
		
	}
	
	@PostMapping("/process-update")
	public String updateContact(@ModelAttribute Contact contact, 
			@RequestParam("profileImage") MultipartFile multipartFile, Principal principal, HttpSession session) {
		try {
			Contact oldContact =this.contactRepo.findById(contact.getcID()).get();
			User user = userRepo.getUserByUserName(principal.getName());
			if(multipartFile.isEmpty()) {
				
				contact.setImage(oldContact.getImage());
	
			}
			else {
				
				File delfile =  new ClassPathResource("/static/images").getFile();
				File deletefile = new File(delfile, oldContact.getImage());
				deletefile.delete();
				
				
				contact.setImage(multipartFile.getOriginalFilename());
				System.out.println(multipartFile.getOriginalFilename());
				File file1 = new ClassPathResource("/static/images").getFile();
				System.out.println(file1);
				Files.copy(multipartFile.getInputStream(),
						Paths.get(file1.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename()),
						StandardCopyOption.REPLACE_EXISTING);
				
			}
			contact.setUser(user);
			this.contactRepo.save(contact);
			session.setAttribute("message", new Message("Contact updated successfully", "alert-success"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("message", new Message("Try again!!", "alert-danger"));
			
		}
		return "redirect:/user/" + contact.getcID() + "/contact";
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "profile";
	}
}
