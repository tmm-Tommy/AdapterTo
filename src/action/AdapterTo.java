package action;

import UI.GenerateDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.xml.XmlFile;
import entity.Element;
import utils.MyUtil;

import java.util.ArrayList;

/**
 * @Author Tommy
 * 2021/1/25
 */
public class AdapterTo extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //获取全部项目
        Project project = e.getProject();
        // 获取当前文件对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        String fileName = psiFile.getName();
        StringBuilder initIds = new StringBuilder();
        StringBuilder getIds = new StringBuilder();
        if (fileName.contains(".xml")) {
            try {
                //如果是xml文件
                XmlFile xmlFile = (XmlFile) psiFile;
                ArrayList<Element> elements = new ArrayList<>();
                MyUtil.getIDsFromLayout(xmlFile, elements);
                //获取Id
                for (Element element : elements) {
                    String s_id = element.getValue().split("id/")[1];
                    initIds.append("            private ").append(element.getName()).append(" ").append(s_id).append(";\n");//定义
                    getIds.append("                ").append(s_id).append(" = (").append(element.getName()).append(") view.findViewById(R.id.").append(s_id).append(");\n");
                }
                //填入生成adapter数据
                StringBuffer adapter_text = new StringBuffer();
                String className = MyUtil.formatMyFileName(fileName.split(".xml")[0])+"Adapter";
                adapter_text.append(" public class "+className+" extends BaseAdapter {\n" +
                        "        List<Object> listBeans;\n" +
                        "        Context context;\n" +
                        "\n" +
                        "        public  "+className+" ( List<Object> listBeans, Context context){\n" +
                        "            this.listBeans = listBeans;\n" +
                        "            this.context = context;\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public int getCount() {\n" +
                        "            return listBeans.size();\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public Object getItem(int position) {\n" +
                        "            return listBeans.get(position);\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public long getItemId(int position) {\n" +
                        "            return position;\n" +
                        "        }\n" +
                        "\n" +
                        "        @Override\n" +
                        "        public View getView(int position, View convertView, ViewGroup parent) {\n" +
                        "            if (convertView == null) {\n" +
                        "                convertView = LayoutInflater.from(context).inflate(R.layout." + fileName.split(".xml")[0] + ", null);\n" +
                        "                convertView.setTag(new ViewHolder(convertView));\n" +
                        "            }\n" +
                        "            initializeViews((Object)getItem(position), (ViewHolder) convertView.getTag());\n" +
                        "            return convertView;\n" +
                        "        }\n" +
                        "\n" +
                        "        private void initializeViews(final Object object, final ViewHolder holder) {\n" +
                        "            //TODO implement\n" +
                        "            try {\n" +
                        "            //此处编写你的逻辑代码\n" +
                        "                \n" +
                        "            }catch (Exception e){\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        protected class ViewHolder {\n" +
                        initIds.toString() +
                        "\n" +
                        "            public ViewHolder(View view) {\n" +
                        getIds.toString() +
                        "            }\n" +
                        "        }\n" +
                        "    }");
                //准备生成Ui对话框
                GenerateDialog dialog = new GenerateDialog(adapter_text);
                dialog.setLocation(0,0);
                dialog.setTitle("adapterTo:v1.0简易版");
                dialog.pack();
                dialog.setVisible(true);
                MyUtil.showNotification(project, MessageType.INFO, "生成成功!");
            } catch (Exception e1) {
                MyUtil.showNotification(project, MessageType.INFO, "生成失败!" + e1.toString());
            }

        } else {
            MyUtil.showNotification(project, MessageType.INFO, "不是XML文件");
        }
    }
}
