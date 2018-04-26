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


    protected static final String REG_HTM01 = "<script[^>]*?>.*?<\\/script>"; // 定义script的正则表达式
    protected static final String REG_HTM02 = "<(?!br|/?p)[^>]*>";
    protected static final String REG_HTM03 = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    protected static final String REG_HTM04 = "([\r\n])[\\s]+";
    protected static final String REG_HTM05 = "-->";
    protected static final String REG_HTM06 = "<!--.*";
    protected static final String REG_HTM07 = "<[^>]+>";
    protected static final String REG_HTM08 = "<(.[^>]*)>";
    protected static final String REG_HTM09 = "&(quot|#34);";
    protected static final String REG_HTM10 = "&(amp|#38);";
    protected static final String REG_HTM11 = "&(lt|#60);";
    protected static final String REG_HTM12 = "&(gt|#62);";
    protected static final String REG_HTM13 = "&(nbsp|#160);";
    protected static final String REG_HTM14 = "&(iexcl|#161);";
    protected static final String REG_HTM15 = "&(cent|#162);";
    protected static final String REG_HTM16 = "&(pound|#163);";
    protected static final String REG_HTM17 = "&(copy|#169);";
    protected static final String REG_HTM18 = "&#(\\d+);";
    protected static final String REG_HTM19 = "(\\s*<br.*?>\\s*)+";
    protected static final String REG_HTM20 = "<p[^>]*>\\s*";
    protected static final String REG_HTM21 = "<p[^>]*>\\s*</p>";
    protected static final String REG_HTM22 = "^(\\s*<br.*?>\\s*)*";
    protected static final String REG_HTM23 = "^(\\s*)*";
    protected static final String REG_HTM24 = "(?:&#|#)\\d+";//定义css颜色代码
    protected static final String REG_HTM25 = "<script[^>]*?>.*?</script>";
    protected static final String REG_HTM26 = "<(.[^>]*)>";
    protected static final String REG_HTM27 = "([\\r\\n])[\\s]+";
    protected static final String REG_HTM28 = "-->";
    protected static final String REG_HTM29 = "<!--.*";
    protected static final String REG_HTM30 = "&(quot|#34);";//"\""
    protected static final String REG_HTM31 = "&(amp|#38);";//"&"
    protected static final String REG_HTM32 = "&(lt|#60);";//"<"
    protected static final String REG_HTM33 = "&(gt|#62);";//">"
    protected static final String REG_HTM34 = "&(nbsp|#160);";//" "
    protected static final String REG_HTM35 = "&(iexcl|#161);";//"\xa1"
    protected static final String REG_HTM36 = "&(cent|#162);";//"\xa2"
    protected static final String REG_HTM37 = "&(pound|#163);";//"\xa3"
    protected static final String REG_HTM38 = "&(copy|#169);";//"\xa9"
    protected static final String REG_HTM39 = "&#(\\d+);";

    protected String match(String htmlStr,String regEx,String replace){
        Pattern p_script = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        String newHtmlStr = m_script.replaceAll(replace);
        return newHtmlStr;
    }


}
