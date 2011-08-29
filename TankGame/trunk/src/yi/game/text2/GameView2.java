package yi.game.text2;

import java.util.Random;

import yi.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView2 extends SurfaceView implements Callback {
	private final String tag = "GameView";
	private GameThread gameThread;
	private Sprite player;// ���
	private Sprite[] player_bullets = new Sprite[3];// ����ӵ�
	private Sprite[] enemy_bullets = new Sprite[4];// �����ӵ�
	private Sprite[] enemys = new Sprite[4];
	private TiledLayer background;
	private final int max_width = 14 * 16;// ����-1 ����16
	private final int max_height = 14 * 16;// ����-1 ����16
	private int killed_enemy = 0;// ��Ϸʤ���������Ǵ��ʮֻ�з�̹��
	private Sprite[] explodes=new Sprite[5];
	private Random random = new Random();
	private MediaPlayer mediaPlayer;
	int transform = Sprite.TRANS_NONE;
	int[][] map = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 2, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1 },
			{ 0, 1, -1, -1, -1, 0, 1, 1, 0, 1, 2, 1, 0, 2, 2 },
			{ 0, 1, -1, -1, -1, 0, 1, 1, 0, 1, 2, 1, 0, 2, 2 },
			{ 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 2, 1, 0, 3, 2 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, -1, 2 },
			{ 3, 0, 0, 1, 0, 0, 2, 0, 0, 1, 3, 1, 2, -1, 3 },
			{ 3, 3, 0, 0, 0, 1, 0, 0, 2, 0, 3, 0, 0, -1, 3 },
			{ 0, 1, 1, 1, 3, 3, 3, 2, 0, 0, 3, 1, 0, 1, 1 },
			{ 0, 0, 0, 2, 3, 1, 0, 1, 0, 1, 0, 1, 0, 2, 1 },
			{ 2, 1, 0, 2, 0, 1, 0, 1, 0, 0, 0, 1, 0, 3, 1 },
			{ 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 2, 1, 0, 3, 2 },
			{ 0, 1, 0, 1, 0, 1, -1, 1, 0, 0, 0, 0, 0, 3, 1 },
			{ 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 2, 1 },
			{ 0, 1, 0, 1, 0, 2, 6, 2, 0, 1, 1, 1, 0, 0, 1 }, };

	public GameView2(Context context) {
		super(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		gameThread = new GameThread(holder);

		Resources res = context.getResources();

		player = new Sprite(BitmapFactory.decodeResource(res,// ��ʼ�����
				R.drawable.player1), 16, 16);
		player.setFrameSequence(new int[] { 0, 1 });
		player.setPosition(4 * 16, 14 * 16);

		for (int i = 0; i < player_bullets.length; i++) {// ��ʼ������ӵ�
			player_bullets[i] = new Sprite(BitmapFactory.decodeResource(res,
					R.drawable.bullet), 6, 6);
			player_bullets[i].setFrameSequence(new int[] { 0 });
			player_bullets[i].setVisible(false);
		}

		for (int i = 0; i < enemy_bullets.length; i++) {// ��ʼ�������ӵ�
			enemy_bullets[i] = new Sprite(BitmapFactory.decodeResource(res,
					R.drawable.bullet), 6, 6);
			enemy_bullets[i].setFrameSequence(new int[] { 2 });
			enemy_bullets[i].setVisible(false);
		}

		int a = 0;
		for (int i = 0; i < enemys.length; i++) {// ��ʼ���ĸ�����
			enemys[i] = new Sprite(BitmapFactory.decodeResource(res,
					R.drawable.enemy), 16, 16);
			enemys[i].setFrameSequence(new int[] { a + 16, a + 16 + 1 });
			a += 2;
			switch (i) {
			case 0:
				enemys[i].setPosition(0, 0);
				break;
			case 1:
				enemys[i].setPosition(5 * 16, 0);
				break;
			case 2:
				enemys[i].setPosition(10 * 16, 0);
				break;
			case 3:
				enemys[i].setPosition(14 * 16, 0);
				break;
			}
		}

		for(int i=0;i<explodes.length;i++){//��ʼ��ը��Ч��
			explodes[i]=new Sprite(BitmapFactory.decodeResource(res, R.drawable.explode2),25,25);
			explodes[i].setVisible(false);
			explodes[i].setFrameSequence(new int[]{2,1,0});
		}
		
		background = new TiledLayer(15, 15, BitmapFactory.decodeResource(res,// ��ʼ������
				R.drawable.tile), 16, 16);
		background.createAnimatedTile(4);
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				background.setCell(i, j, map[i][j]);
			}
		}
	}

	private int x, y;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		x = player.getX();
		y = player.getY();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			transform = Sprite.TRANS_NONE;
			player.move(0, -16);
			if (canTankPass(player.getX(), player.getY())) {
				y -= 16;
			}
			player.setPosition(x, y);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			transform = Sprite.TRANS_ROT180;
			player.move(0, 16);
			if (canTankPass(player.getX(), player.getY())) {
				y += 16;
			}
			player.setPosition(x, y);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			transform = Sprite.TRANS_ROT270;
			player.move(-16, 0);
			if (canTankPass(player.getX(), player.getY())) {
				x -= 16;
			}
			player.setPosition(x, y);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			transform = Sprite.TRANS_ROT90;
			player.move(16, 0);
			if (canTankPass(player.getX(), player.getY())) {
				x += 16;
			}
			player.setPosition(x, y);
			break;
		case KeyEvent.KEYCODE_SPACE:
			for (int i = 0; i < player_bullets.length; i++) {
				if (player.isVisible()
						&& player_bullets[i].isVisible() == false) {
					switch (transform) {
					case Sprite.TRANS_NONE:
						player_bullets[i].setPosition(x + 5, y - 6);
						break;
					case Sprite.TRANS_ROT90:
						player_bullets[i].setPosition(x + 16, y + 5);
						break;
					case Sprite.TRANS_ROT180:
						player_bullets[i].setPosition(x + 5, y + 16);
						break;
					case Sprite.TRANS_ROT270:
						player_bullets[i].setPosition(x - 6, y + 5);
						break;
					default:
						break;
					}
					player_bullets[i].setVisible(true);
					player_bullets[i].setTransform(transform);
					break;
				}
			}
			break;
		}
		player.setTransform(transform);
		return super.onKeyDown(keyCode, event);
	}

	private boolean canTankPass(int x, int y) {// ̹���ܾ���ô��
		if (x < 0 || x > max_width || y < 0 || y > max_height) {
			return false;
		}
		int tileID = map[y / 16][x / 16];
		if (tileID == 1 || tileID == 2 || tileID == -1) {
			return false;
		}
		return true;
	}

	private boolean canBulletPass(int x, int y) {
		if (x < 0 || x - 5 > max_width || y < 0 || y - 5 > max_height) {// �ӵ�����������
			return true;
		}
		int tileId = map[y / 16][x / 16];
		if (tileId == -1 || tileId == 0 || tileId == 3) {
			return true;
		}
		return false;
	}

	private boolean isBullerMeetTank(int bx, int by, int ex, int ey) {// �ж��ӵ��Ƿ���е���
		if (bx / 16 == ex / 16 && by / 16 == ey / 16) {
			return true;
		}
		return false;
	}

	private void playMusic(int id){//��������
		mediaPlayer=MediaPlayer.create(getContext(), id);
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}});
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.v(tag, "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(tag, "surfaceCreated");
		gameThread.start();
		playMusic(R.raw.begin);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(tag, "surfaceDestroyed");
		gameThread.run = false;
	}

	class GameThread extends Thread {
		private SurfaceHolder holder;
		private boolean run = true;

		public GameThread(SurfaceHolder holder) {
			this.holder = holder;
		}

		@Override
		public void run() {
			while (run) {
				Canvas c = null;
				try {
					synchronized (holder) {
						c = holder.lockCanvas();
						c.drawRGB(0, 0, 0);
						if (background.getAnimatedTile(-1) == 4) {
							background.setAnimatedTile(-1, 5);
						} else {
							background.setAnimatedTile(-1, 4);
						}
						background.paint(c);

						if (player.isVisible() == false) {// ������̹�˱����������´���
							player.setPosition(4 * 16, 14 * 16);
							player.setVisible(true);
						}
						if (killed_enemy >= 10) {// ���ʮֻ�з�̹��,��Ϸʤ��
							run = false;
							Looper.prepare();
							new AlertDialog.Builder(getContext()).setTitle(
									"��Ϸʤ����").setPositiveButton("ok", null)
									.show();
							Looper.loop();
						}
						if (background.getCell(14, 6) == 0) {// ��Ϸ����
							run = false;
							playMusic(R.raw.lose);
							Looper.prepare();
							new AlertDialog.Builder(getContext()).setTitle(
									"��Ϸ������").setPositiveButton("ok", null)
									.show();
							Looper.loop();
						}

						for (int i = 0; i < enemys.length; i++) {// ��������󣬹̶��㸴��
							if (!enemys[i].isVisible()) {
								enemys[i].setVisible(true);
								switch (i) {
								case 0:
									enemys[i].setPosition(0, 0);
									break;
								case 1:
									enemys[i].setPosition(5 * 16, 0);
									break;
								case 2:
									enemys[i].setPosition(10 * 16, 0);
									break;
								case 3:
									enemys[i].setPosition(14 * 16, 0);
									break;
								default:
									break;
								}
							}
						}
						for(int i=0;i<explodes.length;i++){
							explodes[i].paint(c);
							explodes[i].nextFrame();
							if(explodes[i].getFrame()==0){
								explodes[i].setVisible(false);
							}
						}
						for (int i = 0; i < player_bullets.length; i++) {
							if (player_bullets[i].isVisible()) {// �ӵ��ɼ�������²�ִ�����в���
								player_bullets[i].paint(c);// ���ӵ�

								for (int j = 0; j < enemys.length; j++) {
									if (enemys[j].isVisible()
											&& isBullerMeetTank(
													player_bullets[i].getX(),
													player_bullets[i].getY(),
													enemys[j].getX(), enemys[j]
															.getY())) {// ������ϵ��ˣ�����ͬʱ����
										player_bullets[i].setVisible(false);
										enemys[j].setVisible(false);
										killed_enemy++;
										explodes[j].setVisible(true);//��ը
										explodes[j].setPosition(enemys[j].getX(), enemys[j].getY());
										break;
									}
								}
								
								

								for (int j = 0; j < enemy_bullets.length; j++) {
									if (enemy_bullets[i].isVisible()
											&& isBullerMeetTank(
													player_bullets[i].getX(),
													player_bullets[i].getY(),
													enemy_bullets[j].getX(),
													enemy_bullets[j].getY())){//��������ӵ�����
										player_bullets[i].setVisible(false);
										enemy_bullets[j].setVisible(false);
										break;
									}
										
								}

								if (!canBulletPass(player_bullets[i].getX(),
										player_bullets[i].getY())) {// �ж��ӵ��Ƿ��ܴ���
									player_bullets[i].setVisible(false);
									if (background.getCell(player_bullets[i]
											.getY() / 16, player_bullets[i]
											.getX() / 16) != 2) {
										background.setCell(player_bullets[i]
												.getY() / 16, player_bullets[i]
												.getX() / 16, 0);
										map[player_bullets[i].getY() / 16][player_bullets[i]
												.getX() / 16] = 0;
									}

								}
								if (player_bullets[i].getX() < 0
										|| player_bullets[i].getX() - 5 > max_width
										|| player_bullets[i].getY() < 0
										|| player_bullets[i].getY() - 5 > max_height) {
									player_bullets[i].setVisible(false);
								} else {
									switch (player_bullets[i].getTransform()) {// �����ӵ�ת���Ƕ����ж��ƶ�����
									case Sprite.TRANS_NONE:
										player_bullets[i].move(0, -16);
										break;
									case Sprite.TRANS_ROT90:
										player_bullets[i].move(16, 0);
										break;
									case Sprite.TRANS_ROT180:
										player_bullets[i].move(0, 16);
										break;
									case Sprite.TRANS_ROT270:
										player_bullets[i].move(-16, 0);
										break;
									default:
										break;
									}
								}

							}
						}

						for (int i = 0; i < enemys.length; i++) {// ������
							if (enemys[i].isVisible()) {// ������˿ɼ�
								enemys[i].paint(c);
								enemys[i].setTransform(random.nextInt(4));
								switch (enemys[i].getTransform()) {
								case Sprite.TRANS_NONE:
									if (canTankPass(enemys[i].getX(), enemys[i]
											.getY() + 16)) {
										enemys[i].move(0, 16);
									}
									break;
								case Sprite.TRANS_ROT90:
									if (canTankPass(enemys[i].getX() - 16,
											enemys[i].getY())) {
										enemys[i].move(-16, 0);
									}
									break;
								case Sprite.TRANS_ROT180:
									if (canTankPass(enemys[i].getX(), enemys[i]
											.getY() - 16)) {
										enemys[i].move(0, -16);
									}
									break;
								case Sprite.TRANS_ROT270:
									if (canTankPass(enemys[i].getX() + 16,
											enemys[i].getY())) {
										enemys[i].move(16, 0);
									}
									break;
								default:
									break;
								}
							}
						}

						for (int i = 0; i < enemy_bullets.length; i++) {
							enemy_bullets[i].paint(c);
							if (enemy_bullets[i].getX() < 0
									|| enemy_bullets[i].getX() - 5 > max_width
									|| enemy_bullets[i].getY() < 0
									|| enemy_bullets[i].getY() - 5 > max_height) {// �ӵ�Խ�紦��
								enemy_bullets[i].setVisible(false);
							}
							if (enemy_bullets[i].isVisible() == false) {// �ӵ����ɼ�ʱ����Ϊ�ɼ���λ��Ϊ��Ӧ���˷��䴦
								enemy_bullets[i].setVisible(true);
								x = enemys[i].getX();
								y = enemys[i].getY();
								switch (enemys[i].getTransform()) {
								case Sprite.TRANS_NONE:
									enemy_bullets[i].setPosition(x + 5, y + 16);
									enemy_bullets[i]
											.setTransform(Sprite.TRANS_NONE);
									break;
								case Sprite.TRANS_ROT90:
									enemy_bullets[i].setPosition(x - 6, y + 5);
									enemy_bullets[i]
											.setTransform(Sprite.TRANS_ROT90);
									break;
								case Sprite.TRANS_ROT180:
									enemy_bullets[i].setPosition(x + 5, y - 6);
									enemy_bullets[i]
											.setTransform(Sprite.TRANS_ROT180);
									break;
								case Sprite.TRANS_ROT270:
									enemy_bullets[i].setPosition(x + 16, y + 5);
									enemy_bullets[i]
											.setTransform(Sprite.TRANS_ROT270);
									break;
								default:
									break;
								}
							} else {// �ӵ��ɼ�ʱ
								if (isBullerMeetTank(enemy_bullets[i].getX(),
										enemy_bullets[i].getY(), player.getX(),
										player.getY())) {// ���������ң�����ͬʱ����
									enemy_bullets[i].setVisible(false);
									player.setVisible(false);
									explodes[4].setVisible(true);//��ը
									explodes[4].setPosition(player.getX(), player.getY());
								}
								if (enemy_bullets[i].isVisible()
										&& !canBulletPass(enemy_bullets[i]
												.getX(), enemy_bullets[i]
												.getY())) {// �ж��ӵ��Ƿ��ܴ���
									enemy_bullets[i].setVisible(false);
									if (background.getCell(enemy_bullets[i]
											.getY() / 16, enemy_bullets[i]
											.getX() / 16) != 2) {
										background.setCell(enemy_bullets[i]
												.getY() / 16, enemy_bullets[i]
												.getX() / 16, 0);
										map[enemy_bullets[i].getY() / 16][enemy_bullets[i]
												.getX() / 16] = 0;
									}

								}
								switch (enemy_bullets[i].getTransform()) {// ��ԭ������move
								case Sprite.TRANS_NONE:
									enemy_bullets[i].move(0, 16);
									break;
								case Sprite.TRANS_ROT90:
									enemy_bullets[i].move(-16, 0);
									break;
								case Sprite.TRANS_ROT180:
									enemy_bullets[i].move(0, -16);
									break;
								case Sprite.TRANS_ROT270:
									enemy_bullets[i].move(16, 0);
									break;

								default:
									break;
								}
							}
						}
						player.paint(c);

						Thread.sleep(200);

					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (c != null) {
						holder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
}
