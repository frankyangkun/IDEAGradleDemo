import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import org.json.JSONArray;
//import org.json.JSONObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonRecursion4 {
	/**
	 * 递归判断接口返回的json结构中所有字段的正确性
	 * @param objJson 实际返回         
	 * @param objJson2 api文档正确字段          
	 * @return 判断结果
	 */
	public Map<Integer,String> analysisJson(Object objJson, Object objJson2) {// 参数1是实际返回，参数2是作对比的合法字段
		Map<Integer,String> map = new HashMap<>();
		String res = "json结构验证完毕！";// 方法执行结果
		map.put(0, res);//0代表运行成功，1代表有错误
	
		Object temp = null;// 用来接收下面的一个局部变量，作为全局变量后面用
		// 如果obj为json数组
		if (objJson instanceof JSONArray) {
			JSONArray objArray = (JSONArray) objJson;
			JSONArray objArray2 = (JSONArray) objJson2;
			// 无需判断实际数组长度和合法长度，因为api文档只规定格式，实际个数没法规定
			for (int i = 0; i < objArray.size(); i++) {// 如果是org.json，就是objArray.length()
				analysisJson(objArray.get(i), objArray2.get(0));// 合法的只需要取索引为0的就行了，实际的有多个，每个都需要跟合法的对比
			}
		}
		// 如果为json对象
		else if (objJson instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) objJson;// objJson没有size方法，因为类型是Object，不是jsonObject
			JSONObject jsonObject2 = (JSONObject) objJson2;
			Iterator it = jsonObject.keys();// 实际的key
			Iterator it2 = jsonObject2.keys();// 合法的key
			System.out.println("key字段个数："+jsonObject.size());

			if (jsonObject.size() != jsonObject2.size()) {
				res = "@@@@@缺少字段或有冗余字段！@@@@@";
				map.clear();
				map.put(1, res);
			} else {
				while (it.hasNext()) {
					String key = it.next().toString();
					Object object = jsonObject.get(key);// 获取实际返回key对应的value
					while (it2.hasNext()) {
						String key2 = it2.next().toString();
						Object object2 = jsonObject2.get(key2);// 获取合法的key2对应的value2
						temp = object2;// 合法的二级结构局部变量赋值给全局变量，后面会用
						if (key2.equals(key)) {
							System.out.println("#####实际key1:" + key + "匹配合法的key2:" + key2 + "####");
							// res ="@@@@@实际key1:"+key+"匹配合法的key2:"+key2+"@@@@@";
							//不能这样设置，因为最终只会return一个结果，而迭代多次的内容每次都会被覆盖，只会留下最后一次成功的打印日志，所以只需要把错误结果赋值给res
							break;
						} else {
							res = "@@@@@实际key1:" + key + "与合法key2:" + key2 + "不匹配@@@@";// 把错误结果赋值给res
							map.clear();//清除默认map值
							map.put(1, res);
							break;
						}
					}
					// 如果得到的是数组
					if (object instanceof JSONArray) {
						JSONArray objArray = (JSONArray) object;
						JSONArray tempArray = (JSONArray) temp;// 合法的数组内的内容(key对应的value值)
						analysisJson(objArray, tempArray);
					}
					// 如果key中是一个json对象
					else if (object instanceof JSONObject) {
						analysisJson((JSONObject) object, (JSONObject) temp);// 这里第二个参数需要的是合法json结构的第二级
					}
					// 如果key中是其他
					 else{
//					 System.out.println("["+key+"]:"+object.toString()+" ");
					 }
				}
			}
		}
		return map;
	}
}