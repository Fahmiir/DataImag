package com.ats.DataImage.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ats.DataImage.Model.DataImageModel;
import com.ats.DataImage.Repository.DataImageRepository;

@Service
@Transactional
public class DataImageService {

	@Autowired
	DataImageRepository dir;
	
	public void create(DataImageModel dim) {
		dir.save(dim);
	}
	
	public void edit(DataImageModel dim) {
		dir.save(dim);
	}
	
	public List<DataImageModel> read(){
		return dir.findAll();
	}
	
	public Optional<DataImageModel> getImageById(Integer id){
		return dir.findById(id);
	}
	
	public void delete(int id) {
		dir.deleteById(id);
	}
}
