package com.neusoft.care.common;

import com.neusoft.care.customer.mapper.CheckOutRequestMapper;
import com.neusoft.care.customer.mapper.CustomerMapper;
import com.neusoft.care.customer.mapper.OutingRequestMapper;
import com.neusoft.care.bed.mapper.BedMapper;
import com.neusoft.care.health.mapper.DailyCareMapper;
import com.neusoft.care.nursing.mapper.NursingRecordMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.care.customer.entity.CheckOutRequest;
import com.neusoft.care.customer.entity.Customer;
import com.neusoft.care.customer.entity.OutingRequest;
import com.neusoft.care.bed.entity.Bed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final CustomerMapper customerMapper;
    private final BedMapper bedMapper;
    private final CheckOutRequestMapper checkOutRequestMapper;
    private final OutingRequestMapper outingRequestMapper;
    private final NursingRecordMapper nursingRecordMapper;
    private final DailyCareMapper dailyCareMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("customerTotal", customerMapper.selectCount(null));
        data.put("inHospital", customerMapper.selectCount(new LambdaQueryWrapper<Customer>().eq(Customer::getStatus, 1)));
        data.put("bedTotal", bedMapper.selectCount(null));
        data.put("bedOccupied", bedMapper.selectCount(new LambdaQueryWrapper<Bed>().eq(Bed::getStatus, 1)));
        data.put("bedFree", bedMapper.selectCount(new LambdaQueryWrapper<Bed>().eq(Bed::getStatus, 0)));
        data.put("pendingCheckOut", checkOutRequestMapper.selectCount(new LambdaQueryWrapper<CheckOutRequest>().eq(CheckOutRequest::getStatus, 0)));
        data.put("pendingOuting", outingRequestMapper.selectCount(new LambdaQueryWrapper<OutingRequest>().eq(OutingRequest::getStatus, 0)));
        data.put("nursingRecords", nursingRecordMapper.selectCount(null));
        data.put("dailyCareRecords", dailyCareMapper.selectCount(null));
        return Result.ok(data);
    }
}
