class CreateBankAccounts < ActiveRecord::Migration[5.0]
  def change
    create_table :bank_accounts do |t|
      t.string :name, :nullable => false
      t.string :currency_code, :nullable => false

      t.timestamps
    end
  end
end
