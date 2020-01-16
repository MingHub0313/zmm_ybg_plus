var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		genstyle : {}
	},
	methods : {
		copystyle : function() {
			var id = getSelectedRow('genstyletable', 'id');
			if (id == null) {
				return;
			}
			$.ajax({
				url : "/gen/genstyle_do/copyStyle.do",
				type : "post",
				dataType : "json",
				data:{id:id},
				success : function(data) {
					alert(data.msg);
					vm.query()
				}
			})
		},
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.genstyle = {};
		},
		update : function(event) {
			var id = getSelectedRow('genstyletable', 'id');
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
		},
		saveOrUpdate : function(event) {
			var url = vm.genstyle.id == null ? "/gen/genstyle_do/create.do"
					: "/gen/genstyle_do/update.do";
			$.ajax({
				type : "POST",
				url : rootPath + url,
				contentType : "application/json",
				data : JSON.stringify(vm.genstyle),
				success : function(r) {
					vm.reload();
					alert(r.msg);

				}
			});
		},
		del : function(event) {
			var ids = getSelectedRows('genstyletable', 'id');
			if (ids == null) {
				return;
			}
			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : rootPath + "/gen/genstyle_do/remove.do",
					data : {
						ids : ids
					},
					success : function(r) {

						alert(r.msg);
						vm.query();
					}
				});
			});
		},
		getInfo : function(id) {
			$.get(rootPath + "/gen/genstyle_do/get.do?id=" + id, function(r) {
				vm.genstyle = r.genstyle;
			});
		},
		reload : function(event) {

			vm.showList = true;
			layui.use('table', function() {
				var table = layui.table;
				table.render({
					elem : '#genstyletable' // 选定是那个DIV
					,
					url : rootPath + '/gen/genstyle_do/list.do',
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
						field : 'name',
						title : '框架名称',
						minWidth : 100,
						templet : function(data) {

							return data.name;
						}
					}, {
						field : 'description',
						title : '描述',
						minWidth : 100,
						templet : function(data) {

							return data.description;
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