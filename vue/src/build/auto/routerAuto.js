//本文件由系统自动生成如果需要手动修改路由 请在routerConfig文件修改
import home from '@/views/Home.vue'
import home2 from '@/views/Home2.vue'
import login from '@/views/login.vue'
const page_index = () => import('@/views/page/index.vue') 
import page_children_list from '@/views/page/children/list.vue'
import page_children_list2 from '@/views/page/children/list2.vue'
export default
[
	
	{
		name: "home",
		path: "/home",
		component: home
	},
	
	{
		name: "home2",
		path: "/home2",
		component: home2
	},
	
	{
		name: "login",
		path: "/login",
		component: login
	},
	
	{
		name: "page/index",
		path: "/",
		component: page_index,
		children: 
		[
			
			{
				name: "page/children/list",
				path: "/list",
				component: page_children_list
			},
			
			{
				name: "page/children/list2",
				path: "/list2",
				component: page_children_list2
			}
		]
	}
]