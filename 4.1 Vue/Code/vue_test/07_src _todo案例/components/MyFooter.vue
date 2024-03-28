<template>
  <!-- v-show="total" 当任务数是0的时候，不展示此组件了 -->
  <div class="todo-footer" v-show="total">
    <label>
      <!-- 下面这个方式可以实现 -->
      <!-- <input type="checkbox" :checked="isAll" @change="checkAll" /> -->
      <input type="checkbox" v-model="isAll" />
    </label>
    <span>
      <span>已完成{{ doneTotal }}</span> / 全部{{ total }}
    </span>
    <button class="btn btn-danger" @click="clearAllTodo">清除已完成任务</button>
  </div>
</template>

<script>
export default {
  name: "MyFooter",
  props: ["todos", "checkAllTodo","clearAllTodo"],
  methods: {
    // 选择所有(全选或者全不选)
    checkAll(e) {
      this.checkAllTodo(e.target.checked);
    },
  },
  computed: {
    total() {
      return this.todos.length;
    },
    doneTotal() {
      // 参数1 函数，数组长度是几，这个函数就会被调用几次
      //  此函数参数1 pre 上一次的值
      // 第一次调用的时候，pre是0，因为我们统计的初始值是0，之后开始处理逻辑
      // 第二次调用的时候，pre的值是上一次调用的完成后的值，如果上一次没return的话就是undefined
      // 最后一次调用的返回值就是reduce函数的返回值
      //  此函数参数2 current 当前的值，在这里指的就是todo项
      // 参数2 计数起始数
      return this.todos.reduce((pre, current) => {
        return pre + (current.done ? 1 : 0);
      }, 0);

      // 下面这样可以实现，但是不高端
      // let i = 0;
      // this.todos.forEach((element) => {
      //   if (element.done) i++;
      // });
      // return i;
    },
    // 计算属性完整版写法
    isAll: {
      get(){
        return this.doneTotal === this.total && this.total !== 0;
      },
      set(value){
        this.checkAllTodo(value);
      }
    },
  },
};
</script>

<style scoped>
/*footer*/
.todo-footer {
  height: 40px;
  line-height: 40px;
  padding-left: 6px;
  margin-top: 5px;
}

.todo-footer label {
  display: inline-block;
  margin-right: 20px;
  cursor: pointer;
}

.todo-footer label input {
  position: relative;
  top: -1px;
  vertical-align: middle;
  margin-right: 5px;
}

.todo-footer button {
  float: right;
  margin-top: 5px;
}
</style>