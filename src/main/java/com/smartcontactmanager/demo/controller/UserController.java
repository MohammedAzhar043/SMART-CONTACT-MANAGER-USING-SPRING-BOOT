package com.smartcontactmanager.demo.controller;

import java.io.File;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.demo.dao.ContactRepository;
import com.smartcontactmanager.demo.dao.UserReposotery;
import com.smartcontactmanager.demo.entities.Contact;
import com.smartcontactmanager.demo.entities.User;
import com.smartcontactmanager.demo.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserReposotery userReposotery;

	@Autowired
	private ContactRepository contactRepository;

	// it will run for each handler,method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {

		// model-use to send the data,
		// principal - this we use to get the username
		String userName = principal.getName();
		System.out.println("***************************");
		System.out.println("User name " + userName);

		System.out.println("***************************");
//		 get the user using user name 
		User user = this.userReposotery.getUserByName(userName);
		System.out.println("User" + user);

		// sending the data to user_dashboard
		m.addAttribute("user", user);

	}

	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		/*
		 * // model-use to send the data, //principal - this we use to get the username
		 * String userName = principal.getName();
		 * System.out.println("***************************");
		 * System.out.println("User name " + userName);
		 * 
		 * System.out.println("***************************"); // get the user using user
		 * name User user = this.userReposotery.getUserByName(userName);
		 * System.out.println("User" + user);
		 * 
		 * // sending the data to user_dashboard model.addAttribute("user", user);
		 */
		// sending the title
		model.addAttribute("title", "User DashBoard");

		return "normal/user_dashboard";
	}

	// open add form handler

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		// sending the title
		model.addAttribute("title", "add contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing add contact form

	/* @RequestParam("profileImage") MultipartFile file --this is for file */

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact, Principal principal,
			@RequestParam("profileImage") MultipartFile file, HttpSession session) {

		try {

			String name = principal.getName();

			User user = this.userReposotery.getUserByName(name);

			// processing and uploading file

			if (file.isEmpty()) {
				// if the file is empty ,then print what ever you want
				System.out.println("file is empty");
				// setting default image
				contact.setImage("contact.png");

			} else {

				// upload the image to folder and update the name to contact
				// updating the name to contact
				contact.setImage(file.getOriginalFilename());

				// uploading the image to folder
				File savefile = new ClassPathResource("static/img").getFile();

				/* path this is the class by using which we can generate the path */
				Path path = Paths.get(savefile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				// from,to,what do to(enum)
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("image is uploaded");
			}

			// giving the user to contact(because it is the bidirectional
			contact.setUser(user);
			// adding the contact to user,by getting the list of contact and then adding to
			// the list
			user.getContacts().add(contact);

			// updating the user after adding the contact to the user
			this.userReposotery.save(user);

			System.out.println("Data is : " + contact);

			// message success
			session.setAttribute("message", new Message("your contact is added !! add more", "success"));

		} catch (Exception e) {

			System.out.println(" Error :  " + e.getMessage());
			e.printStackTrace();
			// message failure
			session.setAttribute("message", new Message("something went wrong ,try again", "danger"));
		}
		return "normal/add_contact_form";
	}

	/*
	 * //show contacts
	 * 
	 * @GetMapping("/show-contacts") public String showContacts(Model m,Principal
	 * principal) {
	 * 
	 * m.addAttribute("title","show user contacts");
	 * 
	 * //need to send the list of contacts to view or show-contacts String name =
	 * principal.getName();
	 * 
	 * //get the user User user = this.userReposotery.getUserByName(name);
	 * 
	 * //get all the contacts List<Contact> contacts = user.getContacts();
	 * 
	 * return "normal/show_contacts"; }
	 */

	// show contacts
	// per page =5[n]
	// current page = 0 [page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {

		m.addAttribute("title", "show user contacts");

		// need to send the list of contacts to view or show-contacts

		// getting the name
		String name = principal.getName();
		User user = this.userReposotery.getUserByName(name);

		int id = user.getId();

		/*
		 * PageRequest-- it is having methods named of,which will take two inputs namely
		 * current page and contact per page, this is implementation of pageable
		 * interface
		 */
		Pageable pageable = PageRequest.of(page, 3);
		/* List<Contact> contacts = this.contactRepository.findContactsByUser(id); */

		Page<Contact> contactsByUser = this.contactRepository.findContactsByUser(id, pageable);
		// sending the contacts
		m.addAttribute("contacts", contactsByUser);
		// current page
		m.addAttribute("currentPage", page);
		// total pages
		m.addAttribute("totalPages", contactsByUser.getTotalPages());

		return "normal/show_contacts";
	}

	// showing the particular contact details

	@RequestMapping("/contact/{cId}")
	public String showPerticularContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {

		// getting the details
		Optional<Contact> optional = this.contactRepository.findById(cId);
		Contact contact = optional.get();

		// getting the details of user of contact or contact ka user
		String name = principal.getName();
		User user = this.userReposotery.getUserByName(name);

		// security check
		if (user.getId() == contact.getUser().getId()) {
			// sending to the contact detail page
			model.addAttribute("contact", contact);
		}

		/* model.addAttribute("title","showing the perticular contact details"); */
		/* sending the contact name as title */
		model.addAttribute("title", contact.getName());
		System.out.println("CID " + cId);
		return "normal/contact_details";
	}

	// delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Principal principal, HttpSession httpSession) {

		/* getting the details of the contact to be deleted */
		Optional<Contact> optionalContact = this.contactRepository.findById(cId);
		Contact contact = optionalContact.get();

		/* getting the details of the user how is trying to delete */
		/* iska id aur contact ke user ka id agar same rahega then we will delete */
		String name = principal.getName();
		User user = this.userReposotery.getUserByName(name);

		/*
		 * performing the check now we are checking the user who is trying to delete is
		 * the same user to whom this contact belongs to
		 */
		
		//if (user.getId() == contact.getUser().getId()) {

			/* contact se user ku unlink kardenge */
			/* contact.setUser(null); */
			/* deleting the contact */
			/* this.contactRepository.delete(contact); */
			
			
			
			/* sending the message */
//			httpSession.setAttribute("message", new Message("contact deleted successfully", "success"));
		//}

		
		user.getContacts().remove(contact);
		
		this.userReposotery.save(user);
		
		httpSession.setAttribute("message", new Message("contact deleted successfully", "success"));
		/* redirecting */
		return "redirect:/user/show-contacts/0";
	}

	
	//open update form handler
	
	@PostMapping("/update-contact/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid,Model m) {
		
		//getting the contact to be updated
		Optional<Contact> byId = this.contactRepository.findById(cid);
		Contact contact = byId.get();
		
		
		//sending the title
		m.addAttribute("title","update contact");
		//sending the contact
		m.addAttribute("contact",contact);
		return "normal/update_form";
	}
	
	//update contact handler
	@RequestMapping(value="/process-update",method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Model model,HttpSession session,Principal principal) {
		
		try {
			
			//old contact details
			
			Contact oldcontactDetails = this.contactRepository.findById(contact.getcId()).get();
			
			
			//image..
			if(!file.isEmpty()) {
				
				//file work/rewrite
				
				//delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile,oldcontactDetails.getImage());
				file1.delete();
				
				
				//update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
			
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING );
				
				//setiing the original name in the contact data base
				contact.setImage(file.getOriginalFilename());
			}
			else {
				//if file is empty
				contact.setImage(oldcontactDetails.getImage());
			}
			//getting the user
			String name = principal.getName();
			User user = this.userReposotery.getUserByName(name);
			
			//setting the user in the contact
			contact.setUser(user);
			//saving the contacat
			this.contactRepository.save(contact);
			
			
			//sending the message
			
			session.setAttribute("message", new Message("your contact is updated!! ", "success"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println(" contact name "+contact.getName());
		System.out.println(" contact id "+contact.getcId());
		
		return "redirect:/user/contact/" + contact.getcId();
	}
	
	
	
	//your profile handler
	
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		model.addAttribute("title","profile page");
		return "normal/profile";
	}
}







