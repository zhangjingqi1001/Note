<template>
  <ul class="todo-main">
    <MyItem v-for="todoObjItem in todos" :key="todoObjItem.id"
     :todo="todoObjItem"
     :checkTodo="checkTodo"
     :deleteItem="deleteItem">
    </MyItem >
  </ul>
</template>



<script>
import MyItem from "./MyItem.vue";
// 引入组件

export default {
  components: { MyItem },
  name: "MyList",

  // data() {
  //   return {
  //     todos: [
  //       { id: "0001", title: "抽烟", done: true },
  //       { id: "0002", title: "喝酒", done: false },
  //       { id: "0003", title: "开车", done: true },
  //     ],
  //   };
  // },
  props:['todos','checkTodo','deleteItem']
};
</script>

<style scoped>
.todo-main {
  margin-left: 0px;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding: 0px;
}

.todo-empty {
  height: 40px;
  line-height: 40px;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding-left: 5px;
  margin-top: 10px;
}
</style>