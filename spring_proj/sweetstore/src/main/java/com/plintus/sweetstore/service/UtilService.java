package com.plintus.sweetstore.service;

import com.plintus.sweetstore.domain.User;
import com.plintus.sweetstore.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UtilService {
    public static List<Integer> getIntegerListFromStringList(List<String> array) {
        List<Integer> resultArray = new ArrayList<>();
        for (String item : array) {
            if (!Objects.equals(item, "")) {
                resultArray.add(Integer.parseInt(item));
            }
        }
        return resultArray;
    }

    public static List<String> getStringListFromStringData(String data, String separator) {
        List<String> resultData = new ArrayList<>();
        if (data.contains(separator)) {
            resultData.addAll(Arrays.stream(data.split(separator)).toList());
        } else if (!data.equals("")){
            resultData.add(data);
        }
        return resultData;
    }

    public static List<String> getStringListFromStringData(String data) {
        return getStringListFromStringData(data, ",");
    }

    public static String getCustomerFullname(String firstName, String lastName,
                                      String dadName) {
        return lastName + " " + firstName + " " + dadName;
    }

    public static String[] getCustomerArrayFullname(String fullname) {
        if (fullname == null) {
            String[] result = new String[3];
            result[0] = "";
            result[1] = "";
            result[2] = "";
            return result;
        }
        return fullname.split(" ");
    }
}
