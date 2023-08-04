import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from "./Snake";

export class GameMap extends AcGameObject {
  constructor(ctx, parent, store) {
    super();

    this.ctx = ctx;
    this.parent = parent;
    this.store = store;
    this.L = 0;

    this.rows = 13;//行
    this.cols = 14;//列

    this.inner_walls_count = 20;
    this.walls = [];

    this.snakes = [
      new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
      new Snake({ id: 1, color: "#EC4848", r: 1, c: this.cols - 2 }, this),
    ];
  }

  crete_walls() {
    // 初始化
    const g = this.store.state.pk.gamemap;
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (g[r][c]) {
          this.walls.push(new Wall(r, c, this));
        }
      }
    }
    return true;
  }

  // 添加监听事件
  add_listening_events() {
    this.ctx.canvas.focus();
    const [sanke0, snake1] = this.snakes;
    this.ctx.canvas.addEventListener("keydown", e => {
      if (e.key === "w") sanke0.set_direction(0);
      else if (e.key === "d") sanke0.set_direction(1);
      else if (e.key === "s") sanke0.set_direction(2);
      else if (e.key === "a") sanke0.set_direction(3);
      else if (e.key === "ArrowUp") snake1.set_direction(0);
      else if (e.key === "ArrowRight") snake1.set_direction(1);
      else if (e.key === "ArrowDown") snake1.set_direction(2);
      else if (e.key === "ArrowLeft") snake1.set_direction(3);
    });
  }

  start() {
    this.crete_walls()
    this.add_listening_events();
  }

  // 每一帧更新一次最大正方形的边长
  update_size() {
    this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  // 判断俩条蛇是否都准备好下一回合
  check_ready() {
    for (let snake of this.snakes) {
      if (snake.status !== "idle") return false;
      if (snake.direction === -1) return false;
    }
    return true;
  }

  // 让俩条蛇进入下一回合
  next_step() {
    for (let snake of this.snakes) {
      snake.next_step();
    }
  }

  // 检查某个位置是否合法
  check_valid(cell) {
    for (let wall of this.walls) {
      if (wall.r === cell.r && wall.c === cell.c) return false;
    }
    for (let snake of this.snakes) {
      let k = snake.cells.length;
      if (!snake.check_tail_increasing()) k--;
      for (let i = 0; i < k; i++) {
        if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) return false;
      }
    }
    return true;
  }

  update() {
    this.update_size();
    if (this.check_ready()) {
      this.next_step();
    }
    this.render();
  }

  render() {
    const color_even = "#AAD752", color_odd = "#A2D048";
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even;
        } else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
      }
    }
  }


}
