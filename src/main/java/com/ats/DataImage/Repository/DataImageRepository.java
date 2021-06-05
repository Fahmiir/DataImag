package com.ats.DataImage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ats.DataImage.Model.DataImageModel;

public interface DataImageRepository extends JpaRepository<DataImageModel,Integer>{

}
