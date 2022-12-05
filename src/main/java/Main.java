import persistence.MyBatisConnectionFactory;
import persistence.dao.*;
import persistence.dto.*;

import service.AdminService;
import service.OwnerService;
import service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
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
        userDAO.insertAdmin(UserDTO.builder()
                .id("admin")
                .pw("admin")
                .age(23)
                .name("ADMIN")
                .phone("010-4265-6669")
                .build()
        );
    }

    public static void init() {
        baekRegist();
        momsOwnerRegist();
        adminService.acceptUser(momsOwner().getPk());
        momsRegist(momsOwner());
        adminService.acceptStore(moms().getId());
        insertMenu();
        acceptAllMenu();

        TotalOrdersDTO to = TotalOrdersDTO.builder()
                .store_id(moms().getId())
                .user_pk(baek().getPk())
                .build();

        List<OrdersDTO> list = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            OrdersDTO o = OrdersDTO.builder()
                    .menu_id(1l)
                    .price(6000)
                    .details("")
                    .build();
            list.add(o);
        }

        System.out.println(userService.order(to, list)==1 ? "성공" : "실패");

        ownerService.acceptOrders(ownerService.getTotalOrders(moms().getId()).get(1).getId());

        userService.cancelOrder(userService.getOrders(baek().getPk()).get(1).getId());
        userService.cancelOrder(userService.getOrders(baek().getPk()).get(2).getId());

    }

    public static void momsStatistics() {
        int idx = 0;
        for (StatisticsDTO statistics : ownerService.getStatistics(moms().getId())) {
            System.out.println(idx++ + ". " + statistics.getName() + ", " + statistics.getCount() + ", " + statistics.getPrice());
        }
    }

    public static void acceptAllMenu() {
        List<MenuDTO> list = adminService.getHoldMenuList();
        for (MenuDTO temp : list) {
            adminService.acceptMenu(temp.getId());
        }
    }

    public static void insertMenu() {
        /* 가게 들고 오기 */
        StoreDTO momstouch = moms();

        /* 옵션 등록 */
        insertOptionAll(momstouch.getId());
        List<DetailsDTO> details = detailsDAO.selectAllWithStore_id(momstouch.getId());

        /* 메뉴 등록 */
        classificationDAO.insertClassification(ClassificationDTO.builder().name("고기고기시리즈").store_id(momstouch.getId()).build());
        ClassificationDTO group1 = classificationDAO.selectAllWithStore_id(momstouch.getId()).get(0);
        insertMenu(group1.getId(), "돈까스도련님고기고기", 6000, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());
        insertMenu(group1.getId(), "탕수육도련님고기고기", 5800, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());

        classificationDAO.insertClassification(ClassificationDTO.builder().name("정식시리즈").store_id(momstouch.getId()).build());
        ClassificationDTO group2 = classificationDAO.selectAllWithStore_id(momstouch.getId()).get(1);
        insertMenu(group2.getId(), "제육 김치찌개 정식", 8200, 10, details.get(0).getId(), details.get(1).getId(), details.get(2).getId(), details.get(3).getId());
    }

    public static void insertOptionAll(Long store_id) {
        ownerService.insertDetails(DetailsDTO.builder().name("한솥밥 곱빼기").price(400).store_id(store_id).build());
        ownerService.insertDetails(DetailsDTO.builder().name("현미밥 교체").price(1000).store_id(store_id).build());
        ownerService.insertDetails(DetailsDTO.builder().name("계란후라이").price(1000).store_id(store_id).build());
        ownerService.insertDetails(DetailsDTO.builder().name("청양고추").price(1000).store_id(store_id).build());
    }

    public static void insertMenu(Long group_id, String name, Integer price, Integer stock, Long... options) {
        ownerService.insertMenu(MenuDTO.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .classification_id(group_id)
                .build()
                , Arrays.asList(options));
    }

    public static void momsOwnerRegist() {
        UserDTO moms = UserDTO.builder()
                .id("moms")
                .pw("pw")
                .phone("000-0000-0000")
                .name("맘터사장")
                .age(30).build();

        ownerService.insertOwner(moms);
    }

    public static UserDTO momsOwner() {
        UserDTO moms = UserDTO.builder()
                .id("moms")
                .pw("pw")
                .build();
        return userService.getUserWithId(moms.getId());
    }

    public static void momsRegist(UserDTO 사장) {
        StoreDTO moms = StoreDTO.builder()
                .name("맘스땃쥐")
                .comment("엄마의 마음으로 만듭니다")
                .phone(사장.getPhone())
                .address("KLE445")
                .open_time(LocalDateTime.now())
                .close_time(LocalDateTime.now())
                .user_pk(사장.getPk())
                .build();

        ownerService.insertStore(moms);
    }

    public static StoreDTO moms() {
        StoreDTO moms = null;
        for (StoreDTO temp : adminService.getStoreList()) {
            if (temp.getName().equals("맘스땃쥐")) {
                moms = temp;
            }
        }
        return moms;
    }


    public static void baekRegist() {
        UserDTO baek = UserDTO.builder()
                .id("nm090909")
                .pw("eoghks0014")
                .phone("010-4265-6669")
                .name("백대환")
                .age(22).build();

        userService.insertUser(baek);
    }


    public static UserDTO baek() {
        UserDTO baek = UserDTO.builder()
                .id("nm090909")
                .pw("eoghks0014")
                .build();

        return userService.getUserWithId(baek.getId());
    }
}