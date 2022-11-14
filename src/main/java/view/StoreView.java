package view;

import persistence.dto.StoreDTO;
import persistence.dto.UserDTO;

import java.util.List;

public class StoreView {
    public void storeViewForAdmin(List<StoreDTO> storeList, List<UserDTO> ownerList) {
        for (int idx = 0; idx < storeList.size(); idx++) {
            StoreDTO store = storeList.get(idx);
            UserDTO owner = ownerList.get(idx);
            System.out.println( (idx + 1) + ". " +
                    store.getName() + ", " +
                    store.getComment() + ", " +
                    store.getAddress() + ", " +
                    store.getPhone() + ", " +
                    owner.getName() + ", " +
                    owner.getPhone() + ", " +
                    owner.getId() + ", " +
                    owner.getPw()
            );
        }
    }
}
