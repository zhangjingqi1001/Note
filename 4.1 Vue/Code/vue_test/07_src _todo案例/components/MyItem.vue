<template>
  <li>
    <label>
      <input
        type="checkbox"
        :checked="todo.done"
        @click="handleCheck(todo.id)"
      />
      <span>{{ todo.title }}</span>
    </label>
    <button class="btn btn-danger" @click="toDeleteItem(todo.id)">删除</button>
  </li>
</template>

<script>
export default {
  name: "MyItem",
  props: ["todo", "checkTodo", "deleteItem"],
  methods: {
    //响应勾选的函数
    handleCheck(id) {
      console.log("复选框id:" + id);
      // 通知App组将将对应的todo对象的done值取反
      this.checkTodo(id);
    },
    // 删除item
    toDeleteItem(id) {
      if (confirm("确定删除吗？")) {
        console.log("复选框id:" + id);
        this.deleteItem(id);
      }
    },
  },
};
</script>

<style scoped>
/* 鼠标悬浮时会有一个背景色 */
li:hover {
  background: #ddd;
}
/* 当鼠标悬浮在哪个item时，哪个item的悬浮按钮就会出现 */
li:hover button {
  float: right;
  display: block;
  margin-top: 3px;
}
li button {
  float: right;
  display: none;
  margin-top: 3px;
}

/*item*/
li {
  list-style: none;
  height: 36px;
  line-height: 36px;
  padding: 0 5px;
  border-bottom: 1px solid #ddd;
}

li label {
  float: left;
  cursor: pointer;
}

li label li input {
  vertical-align: middle;
  margin-right: 6px;
  position: relative;
  top: -1px;
}

li:before {
  content: initial;
}

li:last-child {
  border-bottom: none;
}
</style>