package example.controller;

import example.Global.GlobalVariable;
import example.event.EnterAction;
import example.event.MainAction;
import example.gui.MusicUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import example.controller.Controller.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * @Author: wangzhenze
 * @Description:
 * @Date: Created in 2020/2/23 6:41 下午
 */
public class ServerMusicPageController implements StackController {
    //以下是各控件的ID
    @FXML
    private StackPane Server_StackPane_center;//整个页面的pane的id

    @FXML
    private VBox VBox_LocalPage;//控制页面纵向排序的id

    @FXML
    private AnchorPane topLocalTitle;//顶部区域的id

    @FXML
    private HBox HBox_localButton;//顶部区域横向排列的id

    @FXML
    private Label Label_musicNum;//显示本地音乐数量的文本id

    @FXML
    private Label Server_Label_localMusic;//“本地音乐”

    @FXML
    private Button Button_serverSearch;//“刷新服务器”的按钮id
    @FXML
    private Button Button_serverUpload;//“上传到服务器”的按钮id

    @FXML
    private TextField TextField_localSearchText;//“本地搜索”的搜索框id

    @FXML
    private Button Button_localSearch;//“本地搜索”按钮的id

    @FXML
    private AnchorPane AnchorPane_localMusicList;//本地音乐列表的滚动区域id

    @FXML
    private HBox HBox_centerMusicListAndScroll;//控制横向排序的id

    @FXML
    private TableView<MusicUtils> TableView_musicTable2;//显示本地音乐列表的表id

    @FXML
    private TableColumn<MusicUtils, Integer> TableColumn_musicID;//（隐藏）本地音乐的id列

    @FXML
    private TableColumn<MusicUtils, String> TableColumn_musicTitle;//本地音乐的音乐标题列

    @FXML
    private TableColumn<MusicUtils, String> TableColumn_musicSinger;//本地音乐的歌手列

    @FXML
    private TableColumn<MusicUtils, String> TableColumn_albumName;//本地音乐的专辑列

    @FXML
    private TableColumn<MusicUtils, String> TableColumn_musicTimeLength;//本地音乐的时长列

    @FXML
    private TableColumn<MusicUtils, String> TableColumn_musicSize;//本地音乐的大小列

    //以下是响应方法的接口

    public StackPane getServer_StackPane_center() {
        return Server_StackPane_center;
    }

    public VBox getVBox_LocalPage() {
        return VBox_LocalPage;
    }

    public AnchorPane getTopLocalTitle() {
        return topLocalTitle;
    }

    public HBox getHBox_localButton() {
        return HBox_localButton;
    }

    public Label getLabel_musicNum() {
        return Label_musicNum;
    }

    public Button getButton_serverSearch() {
        return Button_serverSearch;
    }

    public Button getButton_serverUpload() {
        return Button_serverUpload;
    }

    public TextField getTextField_localSearchText() {
        return TextField_localSearchText;
    }

    public Button getButton_localSearch() {
        return Button_localSearch;
    }

    public AnchorPane getAnchorPane_localMusicList() {
        return AnchorPane_localMusicList;
    }

    public HBox getHBox_centerMusicListAndScroll() {
        return HBox_centerMusicListAndScroll;
    }

    public TableView<MusicUtils> getTableView_musicTable2() {
        return TableView_musicTable2;
    }

    public TableColumn<MusicUtils, Integer> getTableColumn_musicID() {
        return TableColumn_musicID;
    }

    public TableColumn<MusicUtils, String> getTableColumn_musicTitle() {
        return TableColumn_musicTitle;
    }

    public TableColumn<MusicUtils, String> getTableColumn_musicSinger() {
        return TableColumn_musicSinger;
    }

    public TableColumn<MusicUtils, String> getTableColumn_albumName() {
        return TableColumn_albumName;
    }

    public TableColumn<MusicUtils, String> getTableColumn_musicTimeLength() {
        return TableColumn_musicTimeLength;
    }

    public TableColumn<MusicUtils, String> getTableColumn_musicSize() {
        return TableColumn_musicSize;
    }

    @Override
    public StackPane getStackPane() {
        return getServer_StackPane_center();
    }

    @FXML
    private void onServerSearch(ActionEvent event) {//“播放全部”按钮的响应方法
        ma.serverSearch();
    }
    @FXML
    private void onServerUpload(ActionEvent event) {//“上传到”按钮的响应方法
        ma.serverUpload();
    }
    @FXML
    private void onLocalSearch(ActionEvent event) {//“本地搜索”按钮的响应方法
        TableView_musicTable2.getItems().clear();
        List<MusicUtils> ml = MainAction.searchsong(TextField_localSearchText.getText(), GlobalVariable.SEARCHMODE_HYBRID,"server");
        TableView_musicTable2.getItems().addAll(ml);
    }

    public void initData(MainAction ma, List<MusicUtils> list) {//初始化数据
        setCss();
        this.ma = ma;
//		ArrayList<String> dirList = new ArrayList<>();
//		dirList.add("C:\\");
//		df.init(dirList);
        TextField_localSearchText.setOnKeyPressed(new EnterAction(TextField_localSearchText, Button_localSearch));
        TableView_musicTable2.setItems(FXCollections.observableArrayList(list));
        TableColumn_musicID.setCellValueFactory(new MainAction.IndexFactory<MusicUtils>(TableView_musicTable2));
        TableColumn_musicTitle.setCellValueFactory(new PropertyValueFactory<>("musicTitle"));
        TableColumn_musicSinger.setCellValueFactory(new PropertyValueFactory<>("musicSinger"));
        TableColumn_albumName.setCellValueFactory(new PropertyValueFactory<>("albumName"));
        TableColumn_musicTimeLength.setCellValueFactory(new PropertyValueFactory<>("musicTimeLength"));
        TableColumn_musicSize.setCellValueFactory(new PropertyValueFactory<>("musicSize"));

        TableColumn_musicID.setSortable(false);
        TableView_musicTable2.setOnMouseClicked(ma.tsca);
    }

    private MainAction ma;
//	static Dirfilter df = new Dirfilter();

    private void setCss() {
        TableView_musicTable2.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        Button_localSearch.getStyleClass().remove(0);

        Server_Label_localMusic.getStyleClass().add("lightLabel");
        TextField_localSearchText.getStyleClass().add("ListField");
        Button_serverSearch.getStyleClass().add("lightButton");
        TableColumn_musicID.getStyleClass().add("tableColumn");
        TableColumn_musicTitle.getStyleClass().add("tableColumn");
        TableColumn_musicSinger.getStyleClass().add("tableColumn");
        TableColumn_albumName.getStyleClass().add("tableColumn");
        TableColumn_musicTimeLength.getStyleClass().add("tableColumn");
        TableColumn_musicSize.getStyleClass().add("tableColumn");

    }
}

//class Dirfilter{
//	static final String titlestr = "选择本地音乐文件夹",
//			introstr = "将自动扫描你勾选的目录，文件增删实时同步。";
//	static final Label title = new Label(titlestr), intro = new Label(introstr);
//	static final Button ok = new Button("确认"),
//			add = new Button("添加文件夹");
//	private static Dirfilter dirfilter;
//	private HBox hb = new HBox();
//	private VBox vb = new VBox();
//	private ArrayList<String> dirList;
//	private ListView<CheckBox> diropt = new ListView<>();
//	private static ArrayList<String> defaultEmptyList = new ArrayList<>();
//	private Stage stage;
//
//	void init() {
//		dirList = defaultEmptyList;
//		setCss();
//		hb.getChildren().addAll(ok,add);
//		vb.getChildren().addAll(title,intro,diropt,hb);
//
//		stage = new Stage();
//		stage.initModality(Modality.APPLICATION_MODAL);
//		Scene scene = new Scene(vb,400,400);
//		scene.getStylesheets().addAll("example/css/local/LocalMusicPageCss.css");
//		//stage.initStyle(StageStyle.UNDECORATED); //待后期实现按钮功能后去掉标题栏
//		stage.setScene(scene);
//	}
//
//	public void setList(ArrayList<String> list) {
//		dirList = list;
//		for(String s : dirList) {
//			CheckBox cb = new CheckBox(s);
//			cb.getStyleClass().add("checkbox");
//			diropt.getItems().add(cb);
//		}
//	}
//
//	public static Dirfilter getDirfilter() {
//		if(dirfilter == null) {
//			dirfilter = new Dirfilter();
//		}
//		return dirfilter;
//	}
//
//	void show() {
//		stage.show();
//		stage.requestFocus();
//	}
//
//	void setCss(){
//		title.getStyleClass().add("lightLabel");
//		title.setFont(Font.font(20));
//		intro.getStyleClass().add("lightLabel");
//		ok.getStyleClass().add("lightButton");
//		add.getStyleClass().add("lightButton");
//		hb.setAlignment(Pos.CENTER); hb.setSpacing(150);
//		hb.getStyleClass().add("lightLabel");
//		vb.getStyleClass().add("lightLabel");
//	}
//}