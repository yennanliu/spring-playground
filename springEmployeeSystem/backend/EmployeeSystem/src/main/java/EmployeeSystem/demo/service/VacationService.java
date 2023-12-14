package EmployeeSystem.demo.service;

import EmployeeSystem.demo.model.Vacation;
import EmployeeSystem.demo.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacationService {

    @Autowired
    VacationRepository vacationRepository;

    public List<Vacation> getVacations() {

        return vacationRepository.findAll();
    }

    public Vacation getVacationById(Integer vacationId) {

        return vacationRepository.findById(vacationId).get();
    }

}
