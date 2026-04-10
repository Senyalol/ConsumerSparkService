package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.SegmentUserDTO;
import com.bankSpark.analyticsService.ORM.Segmentuser;
import com.bankSpark.analyticsService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SegmentUMapper {

    private final UserRepository userRepository;

    @Autowired
    public SegmentUMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Из сущности в DTO
    public SegmentUserDTO toDTO(Segmentuser segmentuser) {

        SegmentUserDTO segmentUserDTO = new SegmentUserDTO();
        segmentUserDTO.setUSegmentId(segmentuser.getId());
        segmentUserDTO.setUserId(segmentuser.getUser().getId());
        segmentUserDTO.setSegment(segmentuser.getSegment());
        segmentUserDTO.setRMinutes(segmentuser.getRMinutes());
        segmentUserDTO.setF(segmentuser.getF());
        segmentUserDTO.setM(segmentuser.getM());
        segmentUserDTO.setUpdatedAt(segmentuser.getUpdatedAt());

        return segmentUserDTO;
    }

    //Из DTO в сущность
    public Segmentuser toEntity(SegmentUserDTO segmentUserDTO) {

        Segmentuser segmentuser = new Segmentuser();
        segmentuser.setId(segmentUserDTO.getUSegmentId());
        segmentuser.setUser(userRepository.findById(segmentUserDTO.getUserId()).get());
        segmentuser.setSegment(segmentUserDTO.getSegment());
        segmentuser.setRMinutes(segmentUserDTO.getRMinutes());
        segmentuser.setF(segmentUserDTO.getF());
        segmentuser.setM(segmentUserDTO.getM());
        segmentuser.setUpdatedAt(segmentUserDTO.getUpdatedAt());

        return segmentuser;
    }

}