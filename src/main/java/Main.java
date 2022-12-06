import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;

import persistence.enums.RegistStatus;
import service.AdminService;
import service.OwnerService;
import service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main { // temp
    static StoreDAO storeDAO = new StoreDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static DetailsDAO detailsDAO = new DetailsDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static MenuDAO menuDAO = new MenuDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static OrdersDAO ordersDAO = new OrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ReviewDAO reviewDAO = new ReviewDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static UserDAO userDAO = new UserDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static ClassificationDAO classificationDAO = new ClassificationDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static TotalOrdersDAO totalOrdersDAO = new TotalOrdersDAO(MyBatisConnectionFactory.getSqlSessionFactory());
    static StatisticsDAO statisticsDAO = new StatisticsDAO(MyBatisConnectionFactory.getSqlSessionFactory());

    static UserService userService = new UserService(ordersDAO, storeDAO, userDAO, menuDAO, reviewDAO, totalOrdersDAO, classificationDAO, detailsDAO);
    static OwnerService ownerService = new OwnerService(userDAO, storeDAO, menuDAO, totalOrdersDAO, ordersDAO, reviewDAO, classificationDAO, statisticsDAO, detailsDAO);
    static AdminService adminService = new AdminService(storeDAO, userDAO, menuDAO, totalOrdersDAO, statisticsDAO);

    static UserDTO user;
    public static void main(String[] args) {
        /* 관리자 */
        userDAO.insertAdmin(UserDTO.builder()
                .id("admin")
                .pw("admin")
                .age(23)
                .name("ADMIN")
                .phone("010-4265-6669")
                .build()
        );

        /* 점주 */
        userDAO.insertOwner(UserDTO.builder()
                .id("1111")
                .pw("1111")
                .age(40)
                .name("1111")
                .phone("010-1111-1111")
                .build()
        );

        /* 한솥 */
        storeDAO.insertStore(StoreDTO.builder()
                .name("한솥도시락")
                .comment("컴소공의 귀요미 이종현이 정성을 담아 만듭니다")
                .address("금오공대 앞")
                .phone("010-1111-1111")
                .open_time(LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0))
                .close_time(LocalDateTime.of(2000, 1, 1, 23, 59, 59, 0))
                .user_pk(userDAO.selectOneWithId("1111").getPk())
                .build()
        );
        storeDAO.updateStatus(1l, RegistStatus.ACCEPT);

        /* 유저 */
        userDAO.insertUser(UserDTO.builder()
                .id("1")
                .pw("1")
                .age(20)
                .name("1")
                .phone("010-0000-0000")
                .build()
        );

        userDAO.insertUser(UserDTO.builder()
                .id("2")
                .pw("2")
                .age(20)
                .name("2")
                .phone("010-0000-0000")
                .build()
        );

        userDAO.insertUser(UserDTO.builder()
                .id("3")
                .pw("3")
                .age(20)
                .name("3")
                .phone("010-0000-0000")
                .build()
        );

        userDAO.insertUser(UserDTO.builder()
                .id("4")
                .pw("4")
                .age(20)
                .name("4")
                .phone("010-0000-0000")
                .build()
        );
    }
}