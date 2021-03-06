package example.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import example.Global.GlobalVariable;
import example.dao.SongDao;
import example.entity.Song;
import example.entity.Tag;
import example.util.SongUtil;
import example.util.XMLUtil;

/**
 * @author ZTF
 * @version 1.0
 * @date 2017年3月16日 下午9:13:38
 * @Description:
 */
public class SongDaoImpl implements SongDao {
    //删除歌曲
    @Override
    public void deleteSong(String menuName, String songPath ) {
        if (menuName.equals("我喜欢的音乐")) {
            setAllIsLike(songPath, false);
        }
        Document document= XMLUtil.getDoc();
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='" + menuName + "']/song[contains(path,'" + songPath + "')]");
        element.getParent().remove(element);
        List<Element> pathList = document.selectNodes("//song[contains(path,'" + songPath + "')]");
        if (pathList.size() == 1) {
            Element parent = pathList.get(0).getParent();
            if (parent.attributeValue("songMenuName").equals("本地音乐")) {
                boolean remove = parent.remove(pathList.get(0));
            }
        }
        XMLUtil.writeDoc(document);
    }

    //添加歌曲列表
    @Override
    public List<Song> getSongList(String songSheet) {
        Document document = XMLUtil.getDoc();
        List<Element> elements = document.getRootElement().elements(songSheet);
        List<Song> songList = new ArrayList<Song>();
        if (elements != null) {
            for (Element e : elements) {
                Song song = SongUtil.eleToSong(e);
                songList.add(song);
            }
        }
        return songList;
    }

    //搜索
    @Override
    public List<Song> getSong(String key, String menuName, int Mode,String where) {
        Document document;
        if(where == "local"){
            document = XMLUtil.getDoc();
        }else{
            document = XMLUtil.getServerDoc();
        }
        List<Element> selectNodes = null;
        if (Mode == GlobalVariable.SEARCHMODE_SONGNAME)
            selectNodes = document.selectNodes("//song-menu[@songMenuName='" + menuName + "']//tag[contains(songName,'" + key + "')]");
        else if (Mode == GlobalVariable.SEARCHMODE_ALBUM)
            selectNodes = document.selectNodes("//song-menu[@songMenuName='" + menuName + "']//tag[contains(album,'" + key + "')]");
        else if (Mode == GlobalVariable.SEARCHMODE_SINGER)
            selectNodes = document.selectNodes("//song-menu[@songMenuName='" + menuName + "']//tag[contains(artist,'" + key + "')]");
        Iterator<Element> iterator = selectNodes.iterator();
        List<Song> songList = new ArrayList<Song>();
        System.out.println("搜索结果" + songList.size());
        while (iterator.hasNext()) {
            Element parent = iterator.next().getParent();
            Song song = SongUtil.eleToSong(parent);
            songList.add(song);
        }
        return songList;
    }

    //添加歌曲
    @Override
    public void addSong(Song song, String menuName) {
        Document document = XMLUtil.getDoc();
        addMusicToLocal(song);
        if (menuName.equals("我喜欢的音乐")) {
            setAllIsLike(song.getPath(), true);
        }
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='" + menuName + "']");
        SongUtil.songToEle(song, element);
        XMLUtil.writeDoc(document);
    }

    //根据文件添加歌曲
    @Override
    public void addSongWithFile(List<Song> songList, String menuName) {
        Document document = XMLUtil.getDoc();
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='" + menuName + "']");
        Iterator<Song> iterator = songList.iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (menuName.equals("我喜欢的音乐")) {
                setAllIsLike(song.getPath(), true);
            }
            SongUtil.songToEle(song, element);
        }
        XMLUtil.writeDoc(document);
    }

    //添加歌曲到本地音乐
    @Override
    public void addMusicToLocal(Song song) {
        Document document = XMLUtil.getDoc();
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='本地音乐']");
        if (!checkMusicExist(song.getPath(), "本地音乐")) {
            SongUtil.songToEle(song, element);
            XMLUtil.writeDoc(document);
        }
    }
    //添加服务器到本地音乐
    @Override
    public void addMusicToServer(Song song) {
        Document document = XMLUtil.getServerDoc();
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='服务器音乐']");
            SongUtil.songToEle(song, element);
            XMLUtil.writeDocServer(document);
    }

    //返回true说明存在，false不存在
    @Override
    public boolean checkMusicExist(String path, String menuName) {
        Document document = XMLUtil.getDoc();
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='" + menuName + "']");
        List<Element> songs = element.elements();
        for (Element s : songs) {
            Song song = SongUtil.eleToSong(s);
            if (song.getPath().equals(path))
                return true;
        }
        return false;
    }

    //设置喜爱
    @Override
    public void setAllIsLike(String path, boolean isLike) {
        Document document = XMLUtil.getDoc();
        List<Element> parentList = document.selectNodes("//song[contains(path,'" + path + "')]");
        for (Element list : parentList) {
            Element element = (Element) list.selectSingleNode("isLike");
            if (isLike) {
                element.setText("YES");
            } else {
                element.setText("NO");
            }
        }
        XMLUtil.writeDoc(document);
    }

    @Override
    public boolean checkPathIsLike(String path) {
        Document document = XMLUtil.getDoc();
        Element element = (Element) document.selectSingleNode("//song-menu[@songMenuName='我喜欢的音乐']");
        Node selectSingleNode = element.selectSingleNode("//song[contains(path,'" + path + "')]");
        if (selectSingleNode != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Song> getSongHYBRID(String key, String menuName,String where ) {
        Document document;
        if(where == "local"){
            document = XMLUtil.getDoc();
        }else{
            document = XMLUtil.getServerDoc();
        }
        List<Element> selectNodes1 = document.selectNodes("//song-menu[@songMenuName='" + menuName + "']//tag[contains(songName,'" + key + "')]");
        ;
        Iterator<Element> iterator1 = selectNodes1.iterator();
        List<Song> songList = new ArrayList<Song>();
        while (iterator1.hasNext()) {
            Element parent = iterator1.next().getParent();
            Song song = SongUtil.eleToSong(parent);
            if (!songList.contains(song))
                songList.add(song);
        }
        List<Element> selectNodes2 = document.selectNodes("//song-menu[@songMenuName='" + menuName + "']//tag[contains(album,'" + key + "')]");
        ;
        Iterator<Element> iterator2 = selectNodes2.iterator();
        while (iterator2.hasNext()) {
            Element parent = iterator2.next().getParent();
            Song song = SongUtil.eleToSong(parent);
            if (!songList.contains(song))
                songList.add(song);
        }
        List<Element> selectNodes3 = document.selectNodes("//song-menu[@songMenuName='" + menuName + "']//tag[contains(artist,'" + key + "')]");
        ;
        Iterator<Element> iterator3 = selectNodes3.iterator();
        while (iterator3.hasNext()) {
            Element parent = iterator3.next().getParent();
            Song song = SongUtil.eleToSong(parent);
            if (!songList.contains(song))
                songList.add(song);
        }
        return songList;
    }

}
 