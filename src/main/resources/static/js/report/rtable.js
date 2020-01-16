var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		rtable : {},
		rtemplate : []
	},
	methods : {
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.rtable = {};

		},
		update : function(event) {
			var id = getSelectedRow('rtabletable', 'id');
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
		},
		saveOrUpdate : function(event) {
			var url = vm.rtable.id == null ? "/report/rtable_do/create.do"
					: "/report/rtable_do/update.do";
			$.ajax({
				type : "POST",
				url : rootPath + url,
				contentType : "application/json",
				data : JSON.stringify(vm.rtable),
				success : function(r) {
					vm.reload();
					alert(r.msg);

				}
			});
		},
		del : function(event) {
			var ids = getSelectedRows('rtabletable', 'id');
			if (ids == null) {
				return;
			}
			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : rootPath + "/report/rtable_do/remove.do",
					data : {
						ids : ids
					},
					success : function(r) {

						alert(r.msg);
						vm.reload();
					}
				});
			});
		},
		getInfo : function(id) {
			$.get(rootPath + "/report/rtable_do/get.do?id=" + id, function(r) {
				vm.rtable = r.rtable;
			});
		},
		gettemplate : function() {
			// 获取excel模版

			$.ajax({
				type : "POST",
				url : rootPath + "/report/rtemplate_do/getOptionlist.do",
				// contentType : "application/json",
				// data : JSON.stringify(vm.rtable),
				dataType : "json",
				success : function(r) {
					vm.rtemplate = r;

				}
			});

		},

		reload : function(event) {

			vm.showList = true;
			layui.use('table', function() {
				var table = layui.table;
				table.render({
					elem : '#rtabletable' // 选定是那个DIV
					,
					url : rootPath + '/report/rtable_do/list.do',
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
						field : 'reportno',
						title : '表格编码',
						minWidth : 100,
						templet : function(data) {

							return data.reportno;
						}
					}, {
						field : 'reportname',
						title : '名称',
						minWidth : 100,
						templet : function(data) {

							return data.reportname;
						}
					}, {
						field : 'reportyear',
						title : '年份',
						minWidth : 100,
						templet : function(data) {

							return data.reportyear;
						}
					}, {
						field : 'dataarea',
						title : '数据区域',
						minWidth : 100,
						templet : function(data) {

							return data.dataarea;
						}
					}, {
						field : 'tempid',
						title : 'EXCEL模版ID',
						minWidth : 100,
						templet : function(data) {

							return data.tempid;
						}
					}, {
						field : 'storetablename',
						title : '数据库表',
						minWidth : 100,
						templet : function(data) {

							return data.storetablename;
						}
					}

					] ],
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
vm.gettemplate();