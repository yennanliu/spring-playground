package EmployeeSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "check_in")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer userID;

    @Column
    private Date createTime;
}
