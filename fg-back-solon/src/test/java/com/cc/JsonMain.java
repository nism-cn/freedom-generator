package com.cc;

import org.nism.fg.base.utils.JsonUtils;
import org.nism.fg.domain.dto.FileDTO;
import org.noear.snack.ONode;
import org.noear.snack.core.Options;

public class JsonMain {
    public static void main(String[] args) {

        FileDTO fileDTO = new FileDTO();


        System.out.println(Options.serialize());
        String s1 = ONode.load(fileDTO, Options.serialize()).toJson();

        System.out.println(s1);

        fileDTO.setRelativePath("abc");

        String s = JsonUtils.writeValueAsString(fileDTO);
        System.out.println(s);
    }
}
