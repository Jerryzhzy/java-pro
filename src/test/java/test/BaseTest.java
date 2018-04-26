package test;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:logback.xml" })
public class BaseTest<T, ID extends Serializable> extends AbstractJUnit4SpringContextTests {
	/*@Autowired
	protected BaseService<T, ID> baseService;

	@org.junit.Test
	public void demo() {
		Date currentDate=new Date(System.currentTimeMillis());
		Test test = new Test();
		test.setName("2");
		test.setGender("2");
		test.setBirthday("2");
		test.setCreatedTime(currentDate);
		test.setModifiedTime(currentDate);
		testService.saveTest(test);
		List<Test> list=testService.findAll(new Specification<Test>() {

			@Override
			public Predicate toPredicate(Root<Test> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return query.getRestriction();
			}
		});

//		PageRequest pageRequest=new PageRequest(1, 10);
//		Page<Test> page=testService.findAll(new Specification<Test>() {
//
//			@Override
//			public Predicate toPredicate(Root<Test> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				List<Predicate> predicates=new ArrayList<>();
//				predicates.add(cb.equal(root.get("name").as(String.class), "123"));
//				Predicate[] pre = new Predicate[predicates.size()];
//				return query.where(predicates.toArray(pre)).getRestriction();
//			}
//		}, pageRequest);
		//Page<Test> page=testService.findAll("from Test t where t.id in('402893ba4f2671b7014f2671fdb20000')", pageRequest);
		Map<String,Object> searchParams=new HashMap<>();
		searchParams.put(Operator.EQ+"_name", "123");
		searchParams.put(Operator.EQ+"_birthday", "12");
//		Pageable pageable=new PageRequest(0, 10, Direction.DESC, "id");
		Page<Test> page=testService.findAll(searchParams, 1, 10, "auto");
//		Page<Test> page=testService.findAll(searchParams, pageable);

		System.out.println("totalElements:"+page.getTotalElements()
				+"\ntotalPages:"+page.getTotalPages()
				+"\nnumberOfElements:"+page.getNumberOfElements());
		List<Test> list=page.getContent();
		for(Test t:list){
			System.out.println(t.getId()+":"+t.getName());
		}
//		System.out.println(testService.findOne("402893ba4f26b998014f26b9cc8f0000").getName());
	}
	@org.junit.Test
	public void demo2(){
		Pageable pageable=new PageRequest((1-1), 10, Direction.DESC, "id");
		Page<Test> page=testService.findAll("from Test t where t.name like '%测试%'", pageable);
//		System.out.println("totalElements:"+page.getTotalElements()
//		+"\ntotalPages:"+page.getTotalPages()
//		+"\nnumberOfElements:"+page.getNumberOfElements());
		List<Test> list=page.getContent();
		System.out.println(page.getTotalElements());
//		for(Test t:list){
//			System.out.println(t.getId()+":"+t.getName());
//		}
	}
	@org.junit.Test
	public void demo3(){
//		Date currentDate=new Date(System.currentTimeMillis());
//		List<Test> list=new ArrayList<>();
//		for(int i=20000;i>=10001;i--){
//			Test test = new Test();
//			test.setName("测试_"+i);
//			test.setGender("男_"+i);
//			test.setBirthday("1990_"+i);
//			test.setCreatedTime(currentDate);
//			test.setModifiedTime(currentDate);
//			list.add(test);
//		}
//		testService.save(list);
	}
	@org.junit.Test
	public void demo4(){
		String sql="select * from s_user";
		List<User> list=testService.findBySql(sql,User.class);
		for(User o:list){
			System.out.println(o.getId());
		}
	}
	@org.junit.Test
	public void demo5(){
		System.out.println(testService.findOne("402893ba4f40ca71014f40ca75520012").getName());
	}
	@org.junit.Test
	public void demo6(){

	}*/

    public static final int tagCount = 200;		 //定义标签总数
    public static final int dataCount = 50000;   //总数据条数
    public static final int tagMax = 200; 		 //随机获取标签范围最大值
    public static final int tagMin = 1; 		 //随机获取标签的最小值
    public static final int tagNum = 200; 		 //随机获取标签的标签总数量
    /**
     * 获取 二进制标识
     * Created by ziyu.zhang on 2016/11/19 16:03
     */
    public String getTag(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<tagCount;i++){
            int rd=Math.random()>0.5?1:0;//是否二进制标识 0否 1是
            sb.append(rd);
        }
        return sb.toString();

    }


    protected static final String regEx_script = "<script[^>]*?>.*?<\\/script>"; // 定义script的正则表达式
    protected static final String regEx_style = "<(?!br|/?p)[^>]*>"; //"" 定义HTML标签的正则表达式
    //protected static final String regEx_style1 = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    protected static final String regEx_html01 = "([\r\n])[\\s]+"; // 定义HTML标签的正则表达式
    protected static final String regEx_html02 = "-->"; // 定义HTML标签的正则表达式
    protected static final String regEx_html03 = "<!--.*"; // 定义HTML标签的正则表达式
    //protected static final String regEx_html04 = "<[^>]+>"; // 定义HTML标签的正则表达式
    //protected static final String regEx_html05 = "<(.[^>]*)>"; // 定义HTML标签的正则表达式
    protected static final String regEx_html06 = "&(quot|#34);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html07 = "&(amp|#38);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html08 = "&(lt|#60);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html09 = "&(gt|#62);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html10 = "&(nbsp|#160);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html11 = "&(iexcl|#161);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html12 = "&(cent|#162);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html13 = "&(pound|#163);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html14 = "&(copy|#169);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html15 = "&#(\\d+);"; // 定义HTML标签的正则表达式
    protected static final String regEx_html16 = "(\\s*<br.*?>\\s*)+"; //"<br/>" 定义HTML标签的正则表达式
    protected static final String regEx_html17 = "<p[^>]*>\\s*"; //"<p>" 定义HTML标签的正则表达式
    protected static final String regEx_html18 = "<p[^>]*>\\s*</p>"; //"" 定义HTML标签的正则表达式
    protected static final String regEx_html19 = "^(\\s*<br.*?>\\s*)*"; //"" 定义HTML标签的正则表达式
    protected static final String regEx_html20 = "^(\\s*)*"; //"" 定义HTML标签的正则表达式
    //protected static final String regEx_color = "(?:&#|#)\\d+";//定义css颜色代码

    //删除脚本
    protected static final String reg_script = "<script[^>]*?>.*?</script>"; //" " 定义HTML标签的正则表达式
    //删除HTML
    protected static final String reg_htm01 = "<(.[^>]*)>"; //" " 定义HTML标签的正则表达式
    protected static final String reg_htm02 = "([\\r\\n])[\\s]+";//" " 定义HTML标签的正则表达式
    protected static final String reg_htm03 = "-->";//" " 定义HTML标签的正则表达式
    protected static final String reg_htm04 = "<!--.*";//" " 定义HTML标签的正则表达式
    protected static final String reg_htm05 = "&(quot|#34);";//"\"" 定义HTML标签的正则表达式
    protected static final String reg_htm06 = "&(amp|#38);";//"&" 定义HTML标签的正则表达式
    protected static final String reg_htm07 = "&(lt|#60);";//"<" 定义HTML标签的正则表达式
    protected static final String reg_htm08 = "&(gt|#62);";//">" 定义HTML标签的正则表达式
    protected static final String reg_htm09 = "&(nbsp|#160);";//" " 定义HTML标签的正则表达式
    protected static final String reg_htm10 = "&(iexcl|#161);";//"\xa1" 定义HTML标签的正则表达式
    protected static final String reg_htm11 = "&(cent|#162);";//"\xa2" 定义HTML标签的正则表达式
    protected static final String reg_htm12 = "&(pound|#163);";//"\xa3" 定义HTML标签的正则表达式
    protected static final String reg_htm13 = "&(copy|#169);";//"\xa9" 定义HTML标签的正则表达式
    protected static final String reg_htm14 = "&#(\\d+);";//" " 定义HTML标签的正则表达式

    protected String match(String htmlStr,String regEx,String replace){
        Pattern p_script = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        String newHtmlStr = m_script.replaceAll(replace);
        return newHtmlStr;
    }
    protected String delHTMLTag1(String htmlStr) {
        String htmlStr1 = match(htmlStr,regEx_script," ");
        String htmlStr2 = match(htmlStr1,regEx_style," ");
        String htmlStr3 = match(htmlStr2,regEx_html01," ");
        String htmlStr4 = match(htmlStr3,regEx_html02," ");
        String htmlStr5 = match(htmlStr4,regEx_html03," ");
        String htmlStr6 = match(htmlStr5,regEx_html06,"\"");
        String htmlStr7 = match(htmlStr6,regEx_html07,"&");
        String htmlStr8 = match(htmlStr7,regEx_html08,"<");
        String htmlStr9 = match(htmlStr8,regEx_html09,">");
        String htmlStr10 = match(htmlStr9,regEx_html10," ");
        String htmlStr11 = match(htmlStr10,regEx_html11,"\\xa1");
        String htmlStr12 = match(htmlStr11,regEx_html12,"\\xa2");
        String htmlStr13 = match(htmlStr12,regEx_html13,"\\xa3");
        String htmlStr14 = match(htmlStr13,regEx_html14,"\\xa9");
        String htmlStr15 = match(htmlStr14,regEx_html15,"");
        String htmlStr16 = match(htmlStr15,regEx_html16," ");//<br/>
        String htmlStr17 = match(htmlStr16,regEx_html17," ");//<p>
        String htmlStr18 = match(htmlStr17,regEx_html18," ");
        String htmlStr19 = match(htmlStr18,regEx_html19," ");
        String htmlStr20 = match(htmlStr19,regEx_html20," ");
        String htmlStr21 = htmlStr20.replaceAll("\\<p>|</p>","");
        return htmlStr21;
    }

}
