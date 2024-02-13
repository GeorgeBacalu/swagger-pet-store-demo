package com.endava.petstore.mock;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.Category;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetMock {

    public static List<Pet> getMockedPets() {
        return List.of(getMockedPet1(), getMockedPet2(), getMockedPet3());
    }

    public static Pet getMockedPet1() {
        return Pet.builder()
              .id(1L)
              .name("Pet1")
              .category(Category.builder().id(1L).name("Category1").build())
              .photoUrls(List.of("https://www.petstore.com/image1.png", "https://www.petstore.com/image2.png"))
              .tags(List.of(
                    Tag.builder().id(1L).name("Tag1").build(),
                    Tag.builder().id(2L).name("Tag2").build()))
              .status(PetStatus.AVAILABLE)
              .build();
    }

    public static Pet getMockedPet2() {
        return Pet.builder()
              .id(2L)
              .name("Pet2")
              .category(Category.builder().id(2L).name("Category2").build())
              .photoUrls(List.of("https://www.petstore.com/image3.png", "https://www.petstore.com/image4.png"))
              .tags(List.of(
                    Tag.builder().id(3L).name("Tag3").build(),
                    Tag.builder().id(4L).name("Tag4").build()))
              .status(PetStatus.PENDING)
              .build();
    }

    public static Pet getMockedPet3() {
        return Pet.builder()
              .id(3L)
              .name("Pet3")
              .category(Category.builder().id(3L).name("Category3").build())
              .photoUrls(List.of("https://www.petstore.com/image5.png", "https://www.petstore.com/image6.png"))
              .tags(List.of(
                    Tag.builder().id(5L).name("Tag5").build(),
                    Tag.builder().id(6L).name("Tag6").build()))
              .status(PetStatus.SOLD)
              .build();
    }
}
