var vm = new Vue(
		{
			el : '#rrapp',
			data : {
				showList : true,
				title : null,
				genDb : {}
			},
			methods : {
				query : function() {
					vm.reload();
				},
				add : function() {
					vm.showList = false;
					vm.title = "新增";
					vm.genDb = {
						dburl : "jdbc:mysql://localhost:3306/quanmin_sys?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=true",
						dbclassdriver : "com.mysql.jdbc.Driver"
					};
				},
				update : function(event) {
					var id = getSelectedRow('genDbtable', 'id');
					if (id == null) {
						return;
					}
					vm.showList = false;
					vm.title = "修改";

					vm.getInfo(id)
				},
				saveOrUpdate : function(event) {
					var url = vm.genDb.id == null ? "/gen/gendb_do/create.do"
							: "/gen/gendb_do/update.do";
					$.ajax({
						type : "POST",
						url : rootPath + url,
						contentType : "application/json",
						data : JSON.stringify(vm.genDb),
						success : function(r) {
							vm.reload();
							alert(r.msg);

						}
					});
				},
				del : function(event) {
					var ids = getSelectedRows('genDbtable', 'id');
					if (ids == null) {
						return;
					}
					confirm('确定要删除选中的记录？', function() {
						$.ajax({
							type : "POST",
							url : rootPath + "/gen/gendb_do/remove.do",
							data : {
								ids : ids
							},
							success : function(r) {

								alert(r.msg);

							}
						});
					});
				},
				getInfo : function(id) {
					$.get(rootPath + "/gen/gendb_do/get.do?id=" + id, function(
							r) {
						vm.genDb = r.genDb;
					});
				},
				reload : function(event) {

					vm.showList = true;
					layui.use('table', function() {
						var table = layui.table;
						table.render({
							elem : '#genDbtable' // 选定是那个DIV
							,
							url : rootPath + '/gen/gendb_do/list.do',
							cols : [ [ {
								type : 'checkbox'
							}, {
								field : 'id',
								title : '主键',
								minWidth : 100,
								templet : function(data) {

									return data.id;
								}
							}, {
								field : 'dbname',
								title : '名称',
								minWidth : 100,
								templet : function(data) {

									return data.dbname;
								}
							}, {
								field : 'dburl',
								title : '数据库地址',
								minWidth : 100,
								templet : function(data) {

									return data.dburl;
								}
							}, {
								field : 'dbusername',
								title : '数据库用户名称',
								minWidth : 100,
								templet : function(data) {

									return data.dbusername;
								}
							}, {
								field : 'dbpwd',
								title : '数据库密码',
								minWidth : 100,
								templet : function(data) {

									return data.dbpwd;
								}
							},{
								field : 'dbclassdriver',
								title : '数据库驱动',
								minWidth : 100,
								templet : function(data) {

									return data.dbclassdriver;
								}
							} ] ],
							page : true, // 开启分页
							request : laypagerequest,
							response : laypageresponse,
							where : $("#searchForm").serializeJSON()
						});
					});
				}

			}
		});

vm.reload();