var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		departmenTable: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.departmenTable = {};
		},
		update: function (event) {
			var id = getSelectedRow('departmenTabletable','id');
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.departmenTable.id == null ? "/report/departmentable_do/create.do" : "/report/departmentable_do/update.do";
			$.ajax({
				type: "POST",
			    url: rootPath + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.departmenTable),
			    success: function(r){
				vm.reload();
				alert(r.msg);
			    	
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows('departmenTabletable','id');
			if(ids == null){
				return ;
			}			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: rootPath + "/report/departmentable_do/remove.do",
				    data : {
                        ids : ids
                    },				   
				    success: function(r){
						
							alert(r.msg);
						
					}
				});
			});
		},
		getInfo: function(id){
			$.get(rootPath + "/report/departmentable_do/get.do?id="+id, function(r){
                vm.departmenTable = r.departmenTable;
            });
		},
		reload: function (event) {
			
	            vm.showList = true;
	           layui.use('table', function() {
                var table = layui.table;
                table.render({
                    elem : '#departmenTabletable' // 选定是那个DIV
                    ,
                    url : rootPath + '/report/departmentable_do/list.do',
                    cols : [
                        [
                            {
                                type : 'checkbox'
                            }
                                                          , {
                                field : 'id',
                                title : '',
                                minWidth : 100,
                                templet : function(data) {
                                  
                                  return data.id;
                                }
                            }                             , {
                                field : 'reportid',
                                title : '',
                                minWidth : 100,
                                templet : function(data) {
                                  
                                  return data.reportid;
                                }
                            }                             , {
                                field : 'departmentid',
                                title : '',
                                minWidth : 100,
                                templet : function(data) {
                                  
                                  return data.departmentid;
                                }
                            }                             ] ],
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