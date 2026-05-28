package com.bankSpark.analyticsService.mapper;

import com.bankSpark.analyticsService.DTO.segmentsRFM.KafkaSegmentUserDTO;
import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.ORM.User;
import com.bankSpark.analyticsService.ORM.segment.SegmentUser;
import com.bankSpark.analyticsService.annotation.Mapper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Mapper
public class SegmentUMapper {

    private static final DecimalFormat R_MINUTES_FORMATTER = new DecimalFormat("#.##");

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        R_MINUTES_FORMATTER.setDecimalFormatSymbols(symbols);
    }


    //Из сущности в DTO
    public SegmentUserDTO toDTO(SegmentUser segmentuser) {

        SegmentUserDTO segmentUserDTO = new SegmentUserDTO();
        segmentUserDTO.setUSegmentId(segmentuser.getId());
        segmentUserDTO.setUserId(segmentuser.getUser().getId());
        segmentUserDTO.setSegment(segmentuser.getSegment());

        if (segmentuser.getRMinutes() != null) {
            segmentUserDTO.setRMinutes(R_MINUTES_FORMATTER.format(segmentuser.getRMinutes()));
        } else {
            segmentUserDTO.setRMinutes("0");
        }

        segmentUserDTO.setF(segmentuser.getF());
        segmentUserDTO.setM(segmentuser.getM());
        String UpdatedAtString = new Date(segmentuser.getUpdatedAt()).toString();
        segmentUserDTO.setUpdatedAt(UpdatedAtString);

        return segmentUserDTO;
    }

    //Из DTO в сущность
//    public SegmentUser toEntity(SegmentUserDTO segmentUserDTO) throws ParseException {
//
//        SegmentUser segmentuser = new SegmentUser();
//        segmentuser.setId(segmentUserDTO.getUSegmentId());
//        segmentuser.setUser(userRepository.findById(segmentUserDTO.getUserId()).get());
//        segmentuser.setSegment(segmentUserDTO.getSegment());
//        segmentuser.setRMinutes(segmentUserDTO.getRMinutes());
//        segmentuser.setF(segmentUserDTO.getF());
//        segmentuser.setM(segmentUserDTO.getM());
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        Date date = sdf.parse(segmentUserDTO.getUpdatedAt());
//        Long trueUpdatedAt = date.getTime();
//        segmentuser.setUpdatedAt(trueUpdatedAt);
//
//        return segmentuser;
//    }

    //В лист DTO
    public List<SegmentUserDTO> toListDTO(List<SegmentUser> segmentUsers) {
        return segmentUsers.stream()
                .map(x -> this.toDTO(x))
                .collect(Collectors.toList());
    }

    //Маппинг DTO в сущность из Kafka
    public SegmentUser fromKafkaDTOtoEntity(KafkaSegmentUserDTO segmentUserDTO, User user) {

        SegmentUser segmentUser = new SegmentUser();
        segmentUser.setUser(user);
        segmentUser.setSegment(segmentUserDTO.getSegment());
        segmentUser.setRMinutes(segmentUserDTO.getR_minutes());
        segmentUser.setF(segmentUserDTO.getF());
        segmentUser.setM(segmentUserDTO.getM());

        segmentUser.setUpdatedAt(segmentUserDTO.getUpdated_at());

        return segmentUser;
    }

}