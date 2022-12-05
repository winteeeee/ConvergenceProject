package network;

import persistence.dto.*;

public class DTOToString {
    public static String classificationDTOToString(ClassificationDTO dto) {
        return "(분류) " + "이름 : " + dto.getName();
    }

    public static String optionDTOToString(DetailsDTO dto) {
        return "(옵션) " + "이름 : " + dto.getName() + ", 가격 : " + dto.getPrice();
    }

    public static String menuDTOToString(MenuDTO dto) {
        return "(메뉴) " + "이름 : " + dto.getName() + ", 상태 : " + dto.getStatus() + ", 가격 : " + dto.getPrice() + ", 재고 : " + dto.getStock();
    }

    public static String orderDTOToString(OrdersDTO dto) {
        return "(주문) " + "옵션 : " + dto.getDetails() + ", 가격 : " + dto.getPrice();
    }

    public static String reviewDTOToString(ReviewDTO dto) {
        return "(리뷰) " + "내용 : " + dto.getComment() + "\n답글 : " + dto.getOwner_comment() + "\n작성날짜 : " + dto.getRegdate() + ", 별점 : " + dto.getStar_rating();
    }

    public static String statisticsDTOToString(StatisticsDTO dto) {
        return "(통계) " + "이름 : " + dto.getName() + ", 가격 : " + dto.getPrice() + ", 개수 : " + dto.getCount();
    }

    public static String storeDTOToString(StoreDTO dto) {
        return "(가게) " + "이름 : " + dto.getName() + ", 점주 전화번호 : " + dto.getPhone() + ", 가게 주소 : " + dto.getAddress()
                + "\n가게 소개 : " + dto.getComment()
                + "\n리뷰 수 : " + dto.getReview_count() + ", 별점 : " + dto.getStar_rating() + ", 여는 시간 : " + dto.getOpen_time() + ", 닫는 시간 : " + dto.getClose_time();
    }

    public static String totalOrderDTOToString(TotalOrdersDTO dto) {
        return "(토탈 오더)  " + "상태 : " + dto.getStatus() + ", 주문일 : " + dto.getRegdate() + ", 가격 : " + dto.getPrice();
    }

    public static String userDTOToString(UserDTO dto) {
        return "(유저) " + "이름 : " + dto.getName() + ", 전화번호 : " + dto.getPhone() + ", 나이 : " + dto.getAge() + ", 상태 : " + dto.getStatus() + ", 권한 : " + dto.getAuthority();
    }
}
