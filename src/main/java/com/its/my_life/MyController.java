package com.its.my_life;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {
    @GetMapping("/") // Bosh sahifa URL
    public String index() {
        return "index";
    }
    @GetMapping("/my_projects")
    public ModelAndView my_projects() {
        List<List<String>> data = get_project_short();
        System.out.println(data.toString());
        ModelAndView modelAndView = new ModelAndView("my_projects");
        modelAndView.addObject("data_list",data);
        return modelAndView;
    }

    @GetMapping("/projects")
    @ResponseBody
    public ModelAndView project_view(@RequestParam("name") String name){
        ModelAndView modelAndView = new ModelAndView("project_view");
        List<List<String>> data = get_project_data(name);
        modelAndView.addObject("data",data);
        System.out.println(data);
        return modelAndView;
    }
    @GetMapping("/my_favourite_music")
    public ModelAndView my_favourite_music() {
        ModelAndView modelAndView = new ModelAndView("my_favourite_music");
        modelAndView.addObject("data",get_my_music());
        return modelAndView;
    }
    @GetMapping("/my_block_posts")
    public ModelAndView my_block_posts() {
        ModelAndView modelAndView = new ModelAndView("my_block_posts");
        modelAndView.addObject("data",get_post_data());
        return modelAndView;
    }
    @GetMapping("/brefly_about_me")
    public String brefly_about_me(){
        return "brefly_about_me";
    }
    private List<List<String>> get_data(){
        List<List<String>> fullList = new ArrayList<>();
        List<String> music_title_list = new ArrayList<>();
        List<String> music_img_list = new ArrayList<>();
        List<String> music_play_and_download_list = new ArrayList<>();
        Connection connection = Jsoup.connect("https://github.com/MyAdmin246/music/tree/main/upload");
        try{
            Elements document = connection.get().select("body").select("div.react-directory-truncate");
            int size = document.size();
            for(int i = 0;i < size; i += 2){
                music_title_list.add(document.get(i).text());
                String to_link = document.get(i).text().replace(" ", "%20");
                music_play_and_download_list.add(String.format("https://github.com/MyAdmin246/music/raw/refs/heads/main/upload/%s/music.mp3", to_link));
                music_img_list.add(String.format("https://github.com/MyAdmin246/music/raw/refs/heads/main/upload/%s/img.png", to_link));
            }
            fullList.add(music_img_list);
            fullList.add(music_title_list);
            fullList.add(music_play_and_download_list);
        }catch(Exception exception){
            fullList = new ArrayList<>();
        }
        return fullList;
    }
    private List<List<String>> get_project_short() {
        List<List<String>> data = new ArrayList<>();
        List<String> list = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        Connection connection = Jsoup.connect("https://github.com/MyAdmin246/projects");
        try {
            Elements document = connection.get().select("tbody").select("div.overflow-hidden");
            int size = document.size();
            for (int i = 0; i < size; i += 2) {
                list.add(document.get(i).select("a").text());
            }
            for (String s : list) {
                Connection d_conn = Jsoup.connect(String.format("https://github.com/MyAdmin246/projects_short/raw/refs/heads/main/%s/title.txt", s));
                try {
                    String title = d_conn.get().body().text();
                    titles.add(title);
                } catch (Exception ignored) {
                }
            }
            for (String s : list) {
                Connection d_conn = Jsoup.connect(String.format("https://github.com/MyAdmin246/projects_short/raw/refs/heads/main/%s/description.txt", s));
                try {
                    String title = d_conn.get().body().text();
                    descriptions.add(title);
                } catch (Exception ignored) {
                }
            }
            for (int b = 0; b < titles.size(); b++) {
                List<String> helper = new ArrayList<>();
                helper.add(titles.get(b));
                helper.add(String.format("https://github.com/MyAdmin246/projects_short/raw/refs/heads/main/%s/logo.png", list.get(b)));
                helper.add(descriptions.get(b));
                data.add(helper);
            }
        } catch (Exception ignored) {}
        return data;
    }
    private List<List<String>> get_project_data(String name){
        List<List<String>> data = new ArrayList<>();
        List<String> p_imgs = new ArrayList<>();
        List<String> p_title = new ArrayList<>();
        List<String> p_description = new ArrayList<>();
        List<String> p_icon = new ArrayList<>();
        List<String> p_download = new ArrayList<>();
        Connection con = Jsoup.connect(String.format("https://github.com/MyAdmin246/projects/tree/main/%s/imgs",name));
        try{
            Elements doc = con.get().select("table.Box-sc-g0xbh4-0 ");
            int size_ = doc.select("tr.react-directory-row ").size();
            for(int i = 0;i < size_;i ++){
                String img_n = doc.select("tr.react-directory-row ").get(i).select("a").attr("title");
                p_imgs.add(String.format("https://github.com/MyAdmin246/projects/raw/refs/heads/main/%s/imgs/%s",name,img_n));
            }
            Connection for_title = Jsoup.connect(String.format("https://github.com/MyAdmin246/projects/raw/refs/heads/main/%s/title.txt",name));
            try{
                p_title.add(for_title.get().body().text());
            }catch(Exception ignored){}
            Connection for_description = Jsoup.connect(String.format("https://github.com/MyAdmin246/projects/raw/refs/heads/main/%s/about.txt",name));
            try{
                p_description.add(for_description.get().body().text());
            }catch(Exception ignored){}
            p_icon.add(String.format("https://github.com/MyAdmin246/projects/raw/refs/heads/main/%s/logo.png",name));
            p_download.add(String.format("https://github.com/MyAdmin246/projects/raw/refs/heads/main/%s/code.zip",name));
            data.add(p_icon);
            data.add(p_title);
            data.add(p_description);
            data.add(p_download);
            data.add(p_imgs);
        }catch(Exception ignored){}
        return data;
    }
    private List<List<String>> get_my_music() {
        List<List<String>> fullList = new ArrayList<>();
        Connection connection = Jsoup.connect("https://github.com/MyAdmin246/music/tree/main/upload");

        try {
            Elements document = connection.get().select("body").select("div.react-directory-truncate");
            int size = document.size();

            for (int i = 0; i < size; i += 2) {
                List<String> helper = new ArrayList<>();
                helper.add(document.get(i).text());
                String to_link = document.get(i).text().replace(" ", "%20");
                helper.add(String.format("https://github.com/MyAdmin246/music/raw/refs/heads/main/upload/%s/music.mp3", to_link));
                helper.add(String.format("https://github.com/MyAdmin246/music/raw/refs/heads/main/upload/%s/img.png", to_link));
                fullList.add(helper);
                System.out.println(fullList.toString());
            }

            System.out.println(fullList.toString());
        } catch (Exception exception) {
            fullList = new ArrayList<>();
        }
        return fullList;
    }
    private List<List<String>> get_post_data(){
        List<List<String>> fullList = new ArrayList<>();

        Connection connection = Jsoup.connect("https://github.com/MyAdmin246/my_posts/tree/main/posts");

        try{
            Elements document = connection.get().select("body").select("div.react-directory-truncate");
            int size = document.size();
            //System.err.println(document);

            for(int i = 0;i < size; i += 2){
                //System.err.println(document.get(i).text());
                List<String> helper = new ArrayList<>();
                String title = document.get(i).text();
                String to_link = document.get(i).text().replace(" ", "%20").replace("‘", "%E2%80%98");
                String data = String.format("https://github.com/MyAdmin246/my_posts/raw/refs/heads/main/posts/%s/text.txt", to_link);
                String image = String.format("https://github.com/MyAdmin246/my_posts/raw/refs/heads/main/posts/%s/image.png", to_link);
                String date = String.format("https://github.com/MyAdmin246/my_posts/raw/refs/heads/main/posts/%s/date.txt", to_link);

                //fullList.add(helper);

                Connection con_date = Jsoup.connect(date);
                String date_result = con_date.get().body().text();
                Connection con_data = Jsoup.connect(data);
                String data_result = con_data.get().body().text();
                helper.add(title);
                helper.add(image);
                helper.add(date_result);
                helper.add(data_result);

                fullList.add(helper);

            }


            System.out.println(fullList.toString());
        }catch(Exception exception){
            fullList = new ArrayList<>();
        }
        return fullList;
    }
}