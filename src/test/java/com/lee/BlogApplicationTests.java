package com.lee;

import com.lee.dao.TagDao;
import com.lee.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class BlogApplicationTests {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private BlogService blogService;

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

//    @Test
//    void contextLoads() {
//
//        String str = "qiqi-dasdas";
//        System.out.println(str);
//        List<Long> longs = convertToList("1,2,3,");
//        List<Tag> tags = new ArrayList<>();
//        for (Long aLong : longs) {
//            tags.add(tagDao.getById(aLong));
//        }
//        for (Tag tag : tags) {
//            System.out.println(tag);
//        }
//    }
//
//
//    @Test
//    void getDetailLog(){
//        DetailedBlog detailedBlog = blogService.getDetailedBlog(1577792888747L);
//        log.info(detailedBlog.toString());
//    }

}
