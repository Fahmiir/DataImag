package com.ats.DataImage.Controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ats.DataImage.Model.DataImageModel;
import com.ats.DataImage.Service.DataImageService;

@Controller
public class DataImageController {

	@Value("${uploadDir}")
	private String uploadFolder;
	
	@Autowired
	DataImageService dis;

	@RequestMapping(value = "/")
	public String menuHome(Model model) {
		List<DataImageModel> dim = new ArrayList<>();
		model.addAttribute("ListDataImageModel",dis.read());
		return "home";
	}

	@RequestMapping(value = "/save")
	public String menuSave(@RequestParam("Nama") String Nama,@RequestParam("Image") MultipartFile Image,HttpServletRequest request) throws IOException, ServletException {
		String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
		String filePath = Paths.get(uploadDirectory, Image.getOriginalFilename()).toString();
		DataImageModel dim = new DataImageModel(); 
		dim.setNama(Nama);
		dim.setPicByte(Image.getBytes()); 
		dim.setImage(Image.getOriginalFilename());
		dim.setType(Image.getContentType());
		dis.create(dim);
		try {
			File dir = new File(uploadDirectory);
			if (!dir.exists()) {
		//		log.info("Folder Created");
				dir.mkdirs();
			}
			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			stream.write(Image.getBytes());
			stream.close();
			System.out.print("good");
		} catch (Exception e) {
//			log.info("in catch");
			e.printStackTrace();
			System.out.print("fail");
		}
		return "redirect:/";
	}
	
	@RequestMapping("/image/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Integer id, HttpServletResponse response, Optional<DataImageModel> imageGallery)
			throws ServletException, IOException {
//		log.info("Id :: " + id);
		imageGallery = dis.getImageById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(imageGallery.get().getPicByte());
		response.getOutputStream().close();
	}
	
	@RequestMapping(value="/edit")
	public String menuEdit(@RequestParam("id") Integer id,@RequestParam("Nama") String Nama,@RequestParam("Image") MultipartFile Image,HttpServletRequest request) throws IOException {
		String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
		String filePath = Paths.get(uploadDirectory, Image.getOriginalFilename()).toString();
		DataImageModel dim = new DataImageModel();
		dim.setId(id);
		dim.setNama(Nama);
		dim.setPicByte(Image.getBytes()); 
		dim.setImage(Image.getOriginalFilename());
		dim.setType(Image.getContentType());
		dis.edit(dim);
		/*
		 * try { File dir = new File(uploadDirectory); if (!dir.exists()) {
		 * dir.mkdirs(); } BufferedOutputStream stream = new BufferedOutputStream(new
		 * FileOutputStream(new File(filePath))); stream.write(Image.getBytes());
		 * stream.close(); } catch (Exception e) { e.printStackTrace(); }
		 */
		return "redirect:/";
	}
	
	@RequestMapping(value="/delete", produces="text/plain")
	@ResponseBody
	public String deleteData(@RequestBody Integer id) {
		dis.delete(id);
		return "redirect:/";
	}

}
